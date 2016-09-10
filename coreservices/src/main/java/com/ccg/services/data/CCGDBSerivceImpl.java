package com.ccg.services.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.ArticleMetaData;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SearchResult2;
import com.ccg.common.data.SubCategoryContent;
import com.ccg.common.data.WCategory;
import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.dao.api.CCGArticleInfoDAO;
import com.ccg.dataaccess.dao.api.CCGArticleMetadataDAO;
import com.ccg.dataaccess.dao.api.CCGCategoryDAO;
import com.ccg.dataaccess.dao.api.CCGContentDAO;
import com.ccg.dataaccess.dao.api.CCGGroupArticleAccessDAO;
import com.ccg.dataaccess.dao.api.CCGSubcategoryDAO;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGArticleInfo;
import com.ccg.dataaccess.entity.CCGArticleMetadata;
import com.ccg.dataaccess.entity.CCGCategory;
import com.ccg.dataaccess.entity.CCGContent;
import com.ccg.dataaccess.entity.CCGGroupArticleAccess;
import com.ccg.dataaccess.entity.CCGSubcategory;
import com.ccg.ingestion.extract.Category;
import com.ccg.services.index.Indexer;
import com.ccg.util.JSON;
import com.ccg.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

@Service("CCGDBService")
public class CCGDBSerivceImpl implements CCGDBService {

	@Autowired
	private CCGArticleInfoDAO articleInfoDAO;
	
	@Autowired	
	private CCGArticleDAO articleDAO;
	
	@Autowired
	private CCGContentDAO contentDAO;
	
	@Autowired
	private CCGCategoryDAO categoryDAO;
	
	@Autowired
	private CCGSubcategoryDAO subcategoryDAO;
	
	@Autowired
	private CCGArticleMetadataDAO metadataDAO;
	
	@Autowired
	private CCGGroupArticleAccessDAO groupArticleAccessDAO;
	
	@Override
	@Transactional(readOnly=false)
	public void saveArticle(CCGArticle article) {
		articleDAO.save(article);		
	}
	
	@Override
	public int getArticleCouont() {
		// TODO Auto-generated method stub
		return articleDAO.countAll();
	}
	@Override
	public String getArticleListJson() {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tmp=new ArrayList<Map<String,Object>>();
		List<CCGArticle> res=articleDAO.findAll();	
		
		for(CCGArticle art:res)
		{
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("text","" + art.getTitle());
			map.put("cls","file");
			map.put("leaf",new Boolean(true));
			map.put("articleID",art.getArticleID()+"");
			tmp.add(map);
		}
		
		return toJson(tmp);
	}
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	@Override
	@Transactional(readOnly=true)
	public List<ArticleBasicInfo> getArticleBasicInfo() {
		
		List<CCGArticle> articles = articleDAO.findAll();
		List<ArticleBasicInfo> infoList = new ArrayList<ArticleBasicInfo>();
		for(CCGArticle article : articles){
			ArticleBasicInfo info = new ArticleBasicInfo();
			info.setArticleID(article.getArticleID());
			info.setTitle(article.getTitle());
			info.setArticleType(article.getArticleType());
			infoList.add(info);
		}
		return infoList;
	}
	@Override
	@Transactional(readOnly=true)
	public ArticleContent getArticleContent(Integer articleId) {
		
		
		
		ArticleContent articleContent = new ArticleContent();
		CCGArticle article = articleDAO.findById(articleId);
		if(article != null){
			CCGContent content = article.getContent();
			articleContent.setContent(content.getContent());
			articleContent.setContentID(content.getContentID());
			articleContent.setContentTitle(content.getContentTitle());
			articleContent.setLength(content.getLength());
			articleContent.setFileName(content.getFilename());
			articleContent.setUrl(content.getUrl());
		}
		return articleContent;
	}
	@Override
	@Transactional(readOnly=true)
	public List<WCategory> getCategoryByArticleId(Integer articleId)
	{
		// first we check if there is articleinfo object
		CCGArticleInfo info=articleInfoDAO.findById(articleId);
		if(info==null)
		{
			// here we are pulling the old TOC from category table
			return getCategoryByArticleIdOld(articleId);
		}
		else
		{
			Category[] toc=JsonHelper.fromJson(info.getToc(), Category[].class);
			List<WCategory> res=new ArrayList<WCategory>();
			for(Category sub:toc)
			{
				res.add(convertCategory(sub));
			}
			return res;
		}
	}
	
	public  WCategory convertCategory(Category c)
	{
		WCategory wc=new WCategory();
		wc.setArticleID(c.getArticleID()+"");
		wc.setCategoryID(-1);
		wc.setCategoryseq(0);
		wc.setCategorytitle(c.getTitle()+" "+c.getStartPage()+"-"+c.getEndPage());
		wc.setEndPage(c.getEndPage());
		wc.setEndposi(c.getEndPosition());
		wc.setStartPage(c.getStartPage());
		wc.setStartposi(c.getStartPosition());
		if(c.getSubCategory()!=null&&c.getSubCategory().size()>0)
		{
			wc.setLeaf(false);
			for(Category sub:c.getSubCategory())
			{
				wc.getSubCategories().add(convertCategory(sub));
			}
		}
		else
		{
			wc.setLeaf(true);
		}
		return wc;

	}
	public List<WCategory> getCategoryByArticleIdOld(Integer articleId) {
		List<WCategory> catList = new ArrayList<WCategory>();
		CCGArticle article = articleDAO.findById(articleId);
		if(article != null){
			List<CCGCategory> ccgCatList = articleDAO.findById(articleId).getCategorylist();
			for(CCGCategory ccgCat : ccgCatList){
				WCategory cat = new WCategory();
				cat.setArticleID(articleId.toString());
				cat.setCategoryID(ccgCat.getCategoryID());
				cat.setCategorytitle(ccgCat.getCategorytitle());
				cat.setCategoryseq(ccgCat.getCategoryseq());
				cat.setStartposi(ccgCat.getStartposi());
				cat.setEndposi(ccgCat.getEndposi());
				cat.setStartPage(ccgCat.getStartpage());
				cat.setEndPage(ccgCat.getEndpage());
				if(ccgCat.getSubcategorylist() != null 
						&& ccgCat.getSubcategorylist().size() != 0){
				//	List<SubCategory> subCatList = new ArrayList<SubCategory>();
					//List<SubCategory> subCatList = new ArrayList<SubCategory>();
					List<CCGSubcategory> ccgSubList = ccgCat.getSubcategorylist();
					for(CCGSubcategory ccgSub : ccgSubList){
						WCategory subCat = new WCategory();
						subCat.setCategoryID(ccgSub.getSubcategoryID());
						subCat.setCategorytitle(ccgSub.getSubcategorytitle());
						subCat.setStartposi(ccgSub.getStartposi());
						subCat.setEndposi(ccgSub.getEndposi());
						subCat.setStartPage(ccgSub.getStartpage());
						subCat.setEndPage(ccgSub.getEndpage());
						cat.getSubCategories().add(subCat);
					}
					cat.setLeaf(false);
				}
				else
				{
					cat.setLeaf(true);
				}
				catList.add(cat);
			}
		}
		return catList;
	}
	
	@Override
	@Transactional(readOnly=true)
	public CategoryContent getCategoryContentById(Integer categoryId) {
		CCGCategory ccgCategory = categoryDAO.findById(categoryId);
		CategoryContent catContent = new CategoryContent();
		if(ccgCategory != null){
			catContent.setArticleID(ccgCategory.getArticle().getArticleID());
			catContent.setCategoryID(categoryId);
			catContent.setCategorytitle(ccgCategory.getCategorytitle());
			catContent.setCategorycontent(ccgCategory.getCategorycontent());
		}
		return catContent;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ArticleContent getContentById(Integer contentId) {
		ArticleContent articleContent = new ArticleContent();
		CCGContent content = contentDAO.findById(contentId);
		if(content != null){
			articleContent.setContent(content.getContent());
			articleContent.setContentID(content.getContentID());
			articleContent.setContentTitle(content.getContentTitle());
			articleContent.setLength(content.getLength());
			articleContent.setFileName(content.getFilename());
		}
		return articleContent;
	}

    @Override
    @Transactional(readOnly=true)
    public SubCategoryContent getSubCategoryContentById(Integer subCategoryId) {
            SubCategoryContent subCatContent = new SubCategoryContent();
            CCGSubcategory subCat = subcategoryDAO.findById(subCategoryId);
            if(subCat != null){
	            CCGArticle article = subCat.getArticle();
	            CCGContent ccgContent = article.getContent();
	            String articleContent = ccgContent.getContent();
	            int startPosition = subCat.getStartposi();
	            int endPosition = subCat.getEndposi();
	            subCatContent.setArticleID(article.getArticleID());
	            subCatContent.setCategoryID(subCat.getCategory().getCategoryID());
	            subCatContent.setSubcategorycontent(articleContent.substring(startPosition, endPosition));
	            subCatContent.setSubcategoryID(subCategoryId);
	            subCatContent.setSubcategorytitle(subCat.getSubcategorytitle());
            }
            return subCatContent;
    }
    
	@Override
	public ArticleMetaData getArticleMetaDataByArticleId(Integer articleId) {
		CCGArticleMetadata ccgMetadata = metadataDAO.findById(articleId);
		ArticleMetaData metadata = new ArticleMetaData();
		if(ccgMetadata != null){
			metadata.setAcceptStatus(ccgMetadata.getAcceptStatus());
			metadata.setAuthor(ccgMetadata.getAuthor());
			metadata.setCompany(ccgMetadata.getCompany());
			metadata.setCreateDate(ccgMetadata.getCreatedTS());
			metadata.setLastUpdateDate(ccgMetadata.getLastUpdateTS());
			metadata.setPraisalscore(ccgMetadata.getPraisalscore());
			metadata.setTitle(ccgMetadata.getTitle());
			metadata.setType(ccgMetadata.getType());
			metadata.setArticleId(articleId);
		}
		return metadata;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void saveOrUpdateArticleMetaData(ArticleMetaData metadata) {
		boolean update = true;
		CCGArticleMetadata ccgMetadata = metadataDAO.findById(metadata.getArticleId());
		
		if(ccgMetadata == null){
			ccgMetadata = new CCGArticleMetadata();
			update = false;
		}
		ccgMetadata.setAcceptStatus(metadata.getAcceptStatus());
		ccgMetadata.setArticleID(metadata.getArticleId());
		ccgMetadata.setAuthor(metadata.getAuthor());
		ccgMetadata.setCompany(metadata.getCompany());
		ccgMetadata.setCreatedTS(metadata.getCreateDate());
		
		if(metadata.getLastUpdateDate() == null){
			ccgMetadata.setLastUpdateTS(new Date());
		}else{
			ccgMetadata.setLastUpdateTS(metadata.getLastUpdateDate());
		}
		
		if(metadata.getPraisalscore() != null)
			ccgMetadata.setPraisalscore(metadata.getPraisalscore());
		ccgMetadata.setTitle(metadata.getTitle());
		ccgMetadata.setType(metadata.getType());

		if(update){		
			metadataDAO.update(ccgMetadata);
		}else{
			metadataDAO.save(ccgMetadata);
		}		
	}

	@Override
	@Transactional(readOnly=false)
	public List<CCGCategory> getAllCategories() {
		return categoryDAO.findAll();
	}
	
	@Override
	//@Transactional(readOnly=false)
	public CCGArticle getCCGArticleById(Integer articleId){
		return articleDAO.findById(articleId);
	}
	
	@Override
	@Transactional(readOnly=false)
	public List<CCGArticle> getAllCCGArticle(){
		return articleDAO.findAll();
	}

	@Override
	@Transactional
	public void deleteArticle(int articleID) {
		// TODO Auto-generated method stub
		articleDAO.delete(articleDAO.findById(articleID));
		articleInfoDAO.delete(articleInfoDAO.findById(articleID));
		List<CCGGroupArticleAccess> entityList = groupArticleAccessDAO.findRecordsByArticleId(articleID);
		for(CCGGroupArticleAccess entity : entityList){
			groupArticleAccessDAO.delete(entity);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public void indexingArticle(Integer articleId){
		CCGArticle article = articleDAO.findById(articleId);
		List<CCGCategory> list = article.getCategorylist();	
		Indexer indexer = new Indexer();
		IndexWriter writer = indexer.getIndexWriter(false);
		for(CCGCategory cat : list){
			indexer.indexingCategory("" + cat.getCategoryID(), cat.getCategorytitle(),
					cat.getCategorycontent(), "" + article.getArticleID(), article.getTitle(), writer);
		}
		
		indexer.closeIndexWriter();
	}
	
	@Override
	@Transactional(readOnly=true)
	public void indexingArticle2(Integer articleId) throws Exception{
		
		Indexer indexer = new Indexer();
		IndexWriter writer = indexer.getIndexWriter(false);
		
		ArticleContent content = getArticleContent(articleId);
		String filename = content.getUrl();
		System.out.println("========== file name====" + filename);
		List<String> pageContents = this.getPdfPageContentAsList(filename);
		int pageNumber = 0;
		
		System.out.println("===== title: " + content.getContentTitle());
		
		for(String pageContent: pageContents){
			indexer.indexingPage(
					"" + articleId, 
					content.getContentTitle(), 
					"" + ++pageNumber, 
					pageContent, 
					writer);
		}
		indexer.closeIndexWriter();
	}	
	
	@Override
	@Transactional(readOnly=true)
	public void indexingAll(){
		List<ArticleBasicInfo> articleList = this.getArticleBasicInfo();
		
		Indexer indexer = new Indexer();
		IndexWriter writer = indexer.getIndexWriter(true);

		for(ArticleBasicInfo info : articleList){
			Integer articleId = info.getArticleID();
			
			CCGArticle article = articleDAO.findById(articleId);
			List<CCGCategory> list = article.getCategorylist();	
						
			for(CCGCategory cat : list){
				indexer.indexingCategory("" + cat.getCategoryID(), cat.getCategorytitle(),
						cat.getCategorycontent(), "" + article.getArticleID(), article.getTitle(), writer);
			}
		}
		
		indexer.closeIndexWriter();
	}

	@Override
	@Transactional(readOnly=true)
	public void indexingAll2() throws Exception{
		List<ArticleBasicInfo> articleList = this.getArticleBasicInfo();
		
		Indexer indexer = new Indexer();
		IndexWriter writer = indexer.getIndexWriter(true);

		for(ArticleBasicInfo info : articleList){
			Integer articleId = info.getArticleID();
			try
			{
			ArticleContent content = getArticleContent(articleId);
			String filename = content.getUrl();
			System.out.println("========== file name====" + filename);
			List<String> pageContents = this.getPdfPageContentAsList(filename);
			int pageNumber = 0;
			for(String pageContent: pageContents){
				indexer.indexingPage(
						"" + articleId, 
						info.getTitle(), 
						"" + ++pageNumber, 
						pageContent, 
						writer);
			}
			}
			catch(Exception e)
			{
				// here continue
			}
		}		
		indexer.closeIndexWriter();
	}	
	
	
	
	@Override
	@Transactional
	public CCGArticleInfo saveArticleInfo(CCGArticleInfo info) {
		// TODO Auto-generated method stub
		return articleInfoDAO.save(info);
	}
	
	private List<String> getPdfPageContentAsList(String filename) throws Exception{
		InputStream is = new FileInputStream(new File(filename));
		List<String> pageContents = new LinkedList<String>();
		try{
			PdfReader reader = new PdfReader(is);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy;
	
			//PDF file page number starts from 1, not 0
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				// System.out.println(strategy.getResultantText());
				pageContents.add(strategy.getResultantText());		
			}
		}finally{
			is.close();
		}
		return pageContents;
		
	}

	@Override
	@Transactional
	public List<WCategory> buildSearchCategory(List<SearchResult2> searchRes,String searchToken) {
		// TODO Auto-generated method stub
		/*
		Collections.sort(searchRes, new Comparator<SearchResult2>(){

			@Override
			public int compare(SearchResult2 arg0, SearchResult2 arg1) {
				// TODO Auto-generated method stub
				return Integer.parseInt(arg0.getPageNumber())-Integer.parseInt(arg1.getPageNumber());
			}
			
		});
		*/
		List<WCategory> res=new ArrayList<WCategory>();  // here we store all the result of the Matched Article
		HashMap<Integer,WCategory> lookupMap=new HashMap<Integer,WCategory>(); // this is duplicate store for lookup ref
		for(SearchResult2 s:searchRes)
		{
			Integer articleID=new Integer(s.getArticleId());
			int pageNumber=Integer.parseInt(s.getPageNumber());
			WCategory a_wc=lookupMap.get(articleID);
			
			if(a_wc==null)
			{
				// lookup article Info
				a_wc=new WCategory();
				a_wc.setArticleID(articleID+"");
				/*
				CCGArticleInfo info=articleInfoDAO.findById(articleID);
				if(info != null){
					
					//Category[] ary=JsonHelper.fromJson(info.getToc(), Category[].class);
					// count query token
					CCGContent content=articleDAO.findById(articleID).getContent();
					int count=JsonHelper.countWord(content.getContent(), searchToken);
					a_wc.setSearchCount(count);
					a_wc.setCategorytitle("Article:["+s.getArticleTitle()+"]" +" --[Matched:"+count+"]");
					// add all children
					
					for(Category c:ary)
					{
						a_wc.getSubCategories().add(convertCategory(c));
					}
					
					// now set start/end page and position
					a_wc.setStartPage(1);				
					a_wc.setStartposi(1);
					//a_wc.setEndPage(ary[ary.length-1].getEndPage());
					//a_wc.setEndposi(ary[ary.length-1].getEndPosition());
					// add to the collection
					res.add(a_wc);
					lookupMap.put(articleID, a_wc);
				}
				*/

				//CCGArticleInfo info=articleInfoDAO.findById(articleID);
				//Category[] ary=JsonHelper.fromJson(info.getToc(), Category[].class);
				// count query token
				CCGContent content=articleDAO.findById(articleID).getContent();
				int count=JsonHelper.countWord(content.getContent(), searchToken);
				a_wc.setSearchCount(count);
				a_wc.setCategorytitle("Article:["+s.getArticleTitle()+"]" +" --[Matched:"+count+"]");
				// add all children
				/*
				for(Category c:ary)
				{
					a_wc.getSubCategories().add(convertCategory(c));
				}
				*/
				// now set start/end page and position
				a_wc.setStartPage(1);				
				a_wc.setStartposi(1);
				a_wc.setEndPage(1);
				a_wc.setEndposi(500);
				// add to the collection
				res.add(a_wc);
				lookupMap.put(articleID, a_wc);

			}
			// now insert new category into the existi category list
			System.out.println("trying to add page...."+pageNumber+" in "+a_wc.getStartPage()+"-"+a_wc.getEndPage());
			WCategory thepage=new WCategory();
			thepage.setArticleID(a_wc.getArticleID());
			thepage.setStartPage(pageNumber);
			thepage.setEndPage(pageNumber);
			thepage.setLeaf(true);
			thepage.setIcon("images/docs.jpg");
			thepage.setCategorytitle("<font color='purple'>[Matched Page: #"+pageNumber+"]</font>");
			a_wc.getSubCategories().add(thepage);			
				
		}
		// convert sorted map to results
		
		Collections.sort(res,new Comparator<WCategory>(){

			@Override
			public int compare(WCategory arg0, WCategory arg1) {
				// TODO Auto-generated method stub
				return arg1.getSearchCount()-arg0.getSearchCount();
			}
			
		});
		
		for(WCategory wc:res)
		{
			wc.setEndPage(wc.getSubCategories().get(0).getStartPage());
			wc.setEndposi(500);
		}
		
		return res;
	}
	
	public boolean addPageIntoCategory(WCategory root,int pageNumber)
	{
		if(root.getStartPage()>pageNumber||root.getEndPage()<pageNumber)
		{
			return false;
		}
		else
		{
			if(root.getSubCategories()!=null&&root.getSubCategories().size()>0)
			{
				// here we need to add the page into sub category
				for(WCategory sub:root.getSubCategories())
				{
					if(addPageIntoCategory(sub,pageNumber))
					{
						return true;
					}
				}
				return false;
			}
			else
			{
				WCategory thepage=new WCategory();
				thepage.setArticleID(root.getArticleID());
				thepage.setStartPage(pageNumber);
				thepage.setEndposi(root.getEndposi());
				thepage.setStartposi(root.getStartposi());
				thepage.setEndPage(pageNumber);
				thepage.setLeaf(true);
				thepage.setIcon("images/docs.jpg");
				thepage.setCategorytitle("<font color='purple'>[Matched Page: #"+pageNumber+"]</font>");
				root.getSubCategories().add(thepage);
				root.setLeaf(false);
				System.out.println("added .....->"+pageNumber);
				return true;
			}
		}
	}
	
	@Override
	public List<SearchResult2> filterDeletedResult( List<SearchResult2> searchResultList){
		
		Set<Integer> yes = new HashSet<Integer>();
		Set<Integer> no = new HashSet<Integer>();
		List<SearchResult2> newList = new ArrayList<SearchResult2>();
		float totalScore = 0f;
		for(SearchResult2 searchResult : searchResultList){
			 String id = searchResult.getArticleId();
			 int articleId = Integer.parseInt(id);
			 if(yes.contains(articleId)){
				 newList.add(searchResult);
			 }else if(no.contains(articleId)){
				 ; // do nothing
			 }else{
				 CCGArticle ccgArticle = articleDAO.findById(articleId);
				 if(ccgArticle != null){
					 newList.add(searchResult);
					 yes.add(articleId);
				 }else{
					 no.add(articleId);
				 }
			 }
		}
		System.out.println("===filtered search results:");
		System.out.println(JSON.toJson(newList));
		
		return newList;
	}

	@Override
	public List<WCategory> getFlatCategory(int articleID, int page) {
		// TODO Auto-generated method stub
		List<WCategory> res=new ArrayList<WCategory>();
		CCGArticleInfo info=articleInfoDAO.findById(articleID);
		Category[] ary=JsonHelper.fromJson(info.getToc(), Category[].class);
		WCategory ac=new WCategory();
		ac.setArticleID(articleID+"");
		ac.setLeaf(false);
		ac.setStartPage(1);
		ac.setEndPage(ary[ary.length-1].getEndPage());
		ac.setCategorytitle("Article:"+articleID);
		res.add(ac);
		for(Category c:ary)
		{
			WCategory wc=convertCategory(c);
			if(matchCategoryWithPage(wc,page,res)) break;
			
		}
		return res;
	}
	
	boolean matchCategoryWithPage(WCategory root, int page,List<WCategory> l)
	{
		boolean found=false;
		if(root.getStartPage()<=page&&root.getEndPage()>=page)
		{
			l.add(root);
			found=true;
			if(root.getSubCategories()!=null&&root.getSubCategories().size()>0)
			{			
				for(WCategory sub:root.getSubCategories())
				{
					if(matchCategoryWithPage(sub,page,l)) break;
				}
			}
			root.getSubCategories().clear();
		}
		if(found)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
