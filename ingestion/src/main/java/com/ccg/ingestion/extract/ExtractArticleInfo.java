package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccg.dataaccess.entity.CCGArticle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ExtractArticleInfo {
	
	private List<PageInfo> pageList = new ArrayList<PageInfo>();
	private ArticleInfo aInfo = new ArticleInfo();
	private List<Integer> pageEndIndex = new ArrayList<Integer>();

	public ArticleInfo fromPDF(InputStream is, String[] pattern) throws IOException {
		aInfo.setType("PDF");

		PdfReader reader = new PdfReader(is);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy;

		aInfo.setPages(reader.getNumberOfPages());
		StringBuffer sb = new StringBuffer();


		//PDF file page number starts from 1, not 0
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			// System.out.println(strategy.getResultantText());
			String temp = strategy.getResultantText() + "\n";
			PageInfo pageInfo = new PageInfo();
			pageInfo.numOfPages = i;
			pageInfo.content = temp;
			pageInfo.numOfChars = temp.length();
			pageList.add(pageInfo);
			sb.append(temp);
		}
		findTitle();
		aInfo.setContent(sb.toString());
		setPageEndIndex();
		
		if(pattern.length > 0){
			// extract level 1 category
			aInfo.setCategoryList(this.extractCategory(aInfo.content, pattern[0], 0));
		}
		
		if(pattern.length > 1){
			// extract level 2 category
			for(Category c : aInfo.getCategoryList()){
				c.setSubCategory(
					extractCategory(aInfo.getContent().substring(c.getStartPosition(), c.getEndPosition()),
					pattern[1], c.getStartPosition()));
			}
		}
		//printPageInfo();
		List<Category> catList = aInfo.getCategoryList();
		catList = this.removeDuplicateCategory(catList);
		aInfo.setCategoryList(catList);
		return aInfo;
	}
	
	private void setPageEndIndex(){
		int counter = 0;
		for(int i = 0; i < pageList.size(); i++){
			counter = counter + pageList.get(i).content.length();
			this.pageEndIndex.add(counter);
		}
//		for(int i= 0; i < this.pageEndIndex.size(); i++){
//			System.out.println(i + 1 + ", " + pageEndIndex.get(i));
//		}
	}
	
	private void printPageInfo(){
		for(PageInfo pageInfo : pageList){
			System.out.println(pageInfo.numOfPages + ", " + pageInfo.numOfChars);
		}
	}
	private void setPageNumberInfoToCategory(List<Category> catList){
		for(Category cat : catList){
			cat.setStartPage(this.indexToPageNumber(cat.getStartPosition()));
			cat.setEndPage(this.indexToPageNumber(cat.getEndPosition()));
		}
	}
	
	private int indexToPageNumber(int index){
		int count = 0;
		int page = -1;
		for(int i = 0; i < this.pageList.size(); i++){
			count = count + pageList.get(i).content.length();
			if(index <= count){
				page = pageList.get(i).numOfPages;
				break;
			}
		}
		return page;
	}
	
	private List<Category> extractCategory(String content, String regex, int offset){
		
		List<Category> cList = new ArrayList<Category>();
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()){
			//System.out.println(matcher.group());
			Category sub = new Category();
			int p0 = matcher.start();
			int p1 = content.indexOf("\n", p0+1);
			if(p1 > p0 && p0 > 0){
				String temp = content.substring(p0, p1);
				sub.setTitle(temp.trim());
				sub.setStartPosition(matcher.start() + offset);
				cList.add(sub);
			}

		}
		
		// find endPosition, the endPositon is the startPositon of next Category
		if(cList.size() > 0){
			Category pre = cList.get(0);
			Category current = null;
			for(int i = 1; i < cList.size(); i++){
				current = cList.get(i);
				pre.setEndPosition(current.getStartPosition() - 1);
				pre = current;			
			}
			if(current == null){
				pre.setEndPosition(content.length() + offset);
			}else{
				current.setEndPosition(content.length() + offset);
			}
		}
		this.setPageNumberInfoToCategory(cList);
		return cList;
	}

	private String findHeader(){
		Map<String, Integer> possibleHeaders = new HashMap<String, Integer>();
		for(PageInfo pInfo: pageList){
			String content = pInfo.content.trim();
			String[] lines = content.split("\n");
			if(lines.length > 0){
				String key = lines[0];
				if(possibleHeaders.containsKey(key)){
					int n = possibleHeaders.get(key);
					possibleHeaders.put(key, ++n);
				}else{
					possibleHeaders.put(key, 1);
				}
			}			
		}
		Set<String> keySet = possibleHeaders.keySet();
		int i = 0;
		String header = "";
		for(String key : keySet){
			int value = possibleHeaders.get(key);
			if(value > i){
				i = value;
				header = key;
			}
		}
		if(i > pageList.size()*2/3){
			return header;
		}else{
			return null;
		}
	}
	
	private String findFooter(){
		Map<String, Integer> possibleFooters = new HashMap<String, Integer>();
		for(PageInfo pInfo: pageList){
			String content = pInfo.content.trim();
			String[] lines = content.split("\n");
			if(lines.length > 0){
				String key = lines[0];
				if(possibleFooters.containsKey(key)){
					int n = possibleFooters.get(key);
					possibleFooters.put(key, ++n);
				}else{
					possibleFooters.put(key, 1);
				}
			}			
		}
		Set<String> keySet = possibleFooters.keySet();
		int i = 0;
		String header = "";
		for(String key : keySet){
			int value = possibleFooters.get(key);
			if(value > i){
				i = value;
				header = key;
			}
		}
		if(i > pageList.size()*2/3){
			return header;
		}else{
			return null;
		}
	}
	
	private void findTitle(){
		// assume first line of first page is Title
		String titleFromFirstLine = null;
		for(int p = 0; p < pageList.size(); p++){
			PageInfo pInfo = pageList.get(0);
			String content = pInfo.content.trim();
			String[] lines = content.split("\n");
			if(lines.length > 0){
				String firstLine = lines[0];
				titleFromFirstLine = firstLine;
				break;
			}
		}
		
		// article title could be in page header. In this case, the title should be 
		// appeared in most of the pages.
		Map<String, Integer> possibleTitle = new HashMap<String, Integer>();
		for(PageInfo pInfo: pageList){
			String content = pInfo.content.trim();
			String[] lines = content.split("\n");
			if(lines.length > 0){
				String key = lines[0];
				if(possibleTitle.containsKey(key)){
					int n = possibleTitle.get(key);
					possibleTitle.put(key, ++n);
				}else{
					possibleTitle.put(key, 1);
				}
			}			
		}
		Set<String> keySet = possibleTitle.keySet();
		int i = 0;
		String titleFromHeader = "";
		for(String key : keySet){
			int value = possibleTitle.get(key);
			if(value > i){
				i = value;
				titleFromHeader = key;
			}
		}
		//
		if( i > pageList.size()*2/3){
			aInfo.setTitle(titleFromHeader);
		}else{
			aInfo.setTitle(titleFromFirstLine);
		}
		
	}
	

	private List<Category> removeDuplicateCategory(List<Category> catList){
		List<Category> newCatList = new ArrayList<Category>();
		int n = 0;
		boolean foundDuplicate = false;
		for( int i = n; i < catList.size() - 1; i++){
			Category cat = catList.get(i);
			String catTitle = cat.getTitle().replace(".","").trim();
			//int contentSize = cat.getContent().length();
			for(int j = i+1; j < catList.size(); j++){
				Category cat2 = catList.get(j);
				String catTitle2 = cat2.getTitle().replace(".","").trim();
				//int contentSize2 = cat2.getContent().length();
				double similarity = StringSimilarity.similarity(catTitle, catTitle2);				
				if(similarity > .60){
					//System.out.println("======= similarity: " + similarity);
					newCatList.add(cat2);
					foundDuplicate = true;
				}
			}
			if(!foundDuplicate){
				// no duplicate found, use original
				newCatList.add(cat);
				foundDuplicate = false;
			}
		}
		
		// do i missed the last one?
		boolean doImissedLastOne = true;
		Category lastOne = catList.get(catList.size() -1);
		for(int i = 0; i < newCatList.size(); i++){
			if(lastOne == newCatList.get(i)){
				doImissedLastOne = false;
			}
		}
		if(doImissedLastOne){
			newCatList.add(lastOne);
		}
		return newCatList;
	}
	
	public static void main(String[] args) throws Exception{
		InputStream is = new FileInputStream(
				new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(is, ArticleTypePattern.PROPOSALS);
		
		List<Category> categoryList = info.getCategoryList();
		List<Category> newcategoryList = extract.removeDuplicateCategory(categoryList);
//		for(Category cat : categoryList){
//			String catTitle = cat.getTitle();
//			System.out.println(catTitle);
//		}
		System.out.println(info.getTitle());
		for(Category cat : categoryList){
			String catTitle = cat.getTitle();
			System.out.println(catTitle);
			int startPage = cat.getStartPage();
			int endPage = cat.getEndPage();
			System.out.println("==Ps=" + startPage + ", Pe=" + endPage);
			
			for(Category sub : cat.getSubCategory()){
				String subTitle = sub.getTitle();
				System.out.println("\t" + subTitle);
				System.out.println("==Ps=" + sub.getStartPage() + ", Pe=" + sub.getEndPage());
			}
		}		
	}	
}

class PageInfo {
	int numOfPages;
	String content;
	int numOfChars;
}
