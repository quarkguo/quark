package com.ccg.services.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.ArticleMetaData;
import com.ccg.common.data.WCategory;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SubCategory;
import com.ccg.common.data.SubCategoryContent;
import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.dao.api.CCGArticleInfoDAO;
import com.ccg.dataaccess.dao.api.CCGArticleMetadataDAO;
import com.ccg.dataaccess.dao.api.CCGCategoryDAO;
import com.ccg.dataaccess.dao.api.CCGContentDAO;
import com.ccg.dataaccess.dao.api.CCGSubcategoryDAO;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGArticleInfo;
import com.ccg.dataaccess.entity.CCGArticleMetadata;
import com.ccg.dataaccess.entity.CCGCategory;
import com.ccg.dataaccess.entity.CCGContent;
import com.ccg.dataaccess.entity.CCGSubcategory;
import com.ccg.ingestion.extract.Category;
import com.ccg.services.index.Indexer;
import com.ccg.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		wc.setCategorytitle(c.getTitle());
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
	@Transactional
	public CCGArticleInfo saveArticleInfo(CCGArticleInfo info) {
		// TODO Auto-generated method stub
		return articleInfoDAO.save(info);
	}
}
