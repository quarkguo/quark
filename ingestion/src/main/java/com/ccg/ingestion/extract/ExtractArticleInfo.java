package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ExtractArticleInfo {
	
	private List<PageInfo> pageList = new ArrayList<PageInfo>();
	private ArticleInfo aInfo = new ArticleInfo();
	private List<Integer> pageEndIndex = new ArrayList<Integer>();
	private List<PossiblePageHeaderOrFooter> possibleHeaderList = new ArrayList<PossiblePageHeaderOrFooter>();
	private List<PossiblePageHeaderOrFooter> possibleFooterList = new ArrayList<PossiblePageHeaderOrFooter>();

	public ArticleInfo fromPDF(InputStream is, String[] pattern) throws IOException {
		aInfo.setType("PDF");

		PdfReader reader = new PdfReader(is);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy;

		aInfo.setPages(reader.getNumberOfPages());		
		
		//PDF file page number starts from 1, not 0
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			// System.out.println(strategy.getResultantText());
			String temp = strategy.getResultantText() + "\n";
			PageInfo pageInfo = new PageInfo();
			pageInfo.pageNumber = i;
			pageInfo.content = temp;
			//pageInfo.numOfChars = temp.length();
			pageList.add(pageInfo);
			//sb.append(temp);
			setPossiblePageHeaderAndFooter(temp, i);
		}
		////////////
		List<String> pageHeader = findPageHeader();
		List<String> pageFooter = findPageFooter();
		removePageHeaderAndFooter(pageHeader, pageFooter);
		setContent();
		setTitle(pageHeader);
		//printPageInfo();
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

		List<Category> catList = aInfo.getCategoryList();
		if(catList.size() == 0){
			String patternString = "";
			for(String s : pattern){
				patternString += s + ", ";
			}
			throw new RuntimeException("Category size is 0, a wrong pattern may used:  )" + patternString);
		}		
		catList = this.removeDuplicateCategory(catList);
		aInfo.setCategoryList(catList);
		
		return aInfo;
	}
	
	private void setTitle(List<String> pageHeader){
		StringBuffer sb = new StringBuffer();
		for(String line : pageHeader){
			sb.append(line).append("\n");
		}
		aInfo.setTitle(sb.toString());
	}
	private void setContent(){
		StringBuffer sb = new StringBuffer();
		for(PageInfo pageInfo : pageList){
			sb.append(pageInfo.content);
		}
		aInfo.setContent(sb.toString());
	}
	
	private void removePageHeaderAndFooter(List<String> pageHeader, List<String> pageFooter){
		int numOfHeaderLine = pageHeader.size();
		int numOfFooterLine = pageFooter.size();
		
		for(int i = 0; i < pageList.size(); i++){
			PageInfo pageInfo = pageList.get(i);
			String content = pageInfo.content;
			String[] lines = content.split("\\n");
			if(lines.length > numOfHeaderLine + numOfFooterLine){
				// remove header
				for(int j = 0; j < numOfHeaderLine; j++){
					String header = pageHeader.get(j);
					double similarity = StringSimilarity.similarity(header, lines[j]);
					if(similarity > 0.6){
						lines[j] = "";
					}
				}
				// remove footer
				for(int k = 0; k < numOfFooterLine; k++ ){
					String footer = pageFooter.get(k);
					int numOfContentLine = lines.length;
					double similarity = StringSimilarity.similarity(
							footer, lines[numOfContentLine - 1 - k]);
					if(similarity > 0.6){
						lines[numOfContentLine -1 - k] = "";
					}
				}
				
			}
			StringBuffer sb = new StringBuffer();
			for(int k = 0 ; k < lines.length; k++){
				sb.append(lines[k]).append("\n");
			}
			pageInfo.content = sb.toString();
			pageInfo.numOfChars = content.length();
		}
		
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
	private List<String> findPageHeader(){
		List<String> headerList = new ArrayList<String>();
		if(this.possibleHeaderList.size() / 2 > 5){
			// more than 10 pages
			// check from middle of the page number
			int startPage = possibleHeaderList.size() / 2;
			PossiblePageHeaderOrFooter header1 = possibleHeaderList.get(startPage);
			PossiblePageHeaderOrFooter header2 = possibleHeaderList.get(startPage + 1);
			double line1_similarity = StringSimilarity.similarity(header1.getLine1(), header2.getLine1());
			System.out.println("===>>> Similarity 1: " + line1_similarity);
			if(line1_similarity > .6 && header1.getLine1().trim().length() != 0){
				headerList.add(header1.getLine1());
				double line2_similarity = StringSimilarity.similarity(header1.getLine2(), header2.getLine2());
				System.out.println("===>>> Similarity 2: " + line2_similarity);
				if(line2_similarity > 0.6 && header1.getLine2().trim().length() != 0){
					headerList.add(header1.getLine2());
					double line3_similarity = StringSimilarity.similarity(header1.getLine3(), header2.getLine3());
					System.out.println("===>>> Similarity 3: " + line3_similarity);
					if(line3_similarity > .6 && header1.getLine3().trim().length() != 0){
						headerList.add(header1.getLine3());
					}
				}
			}
			
			System.out.println("===== Page Header ======");
			System.out.println(">>>" + headerList + "<<<");
		}
		return headerList;
	}
	private List<String> findPageFooter(){
		List<String> footerList = new ArrayList<String>();
		if(this.possibleFooterList.size() / 2 > 5){
			// more than 10 pages
			// check from middle of the page number
			int startPage = possibleFooterList.size() / 2;
			PossiblePageHeaderOrFooter footer1 = possibleFooterList.get(startPage);
			PossiblePageHeaderOrFooter footer2 = possibleFooterList.get(startPage + 1);
			double line1_similarity = StringSimilarity.similarity(footer1.getLine1(), footer2.getLine1());
			System.out.println("===>>> Similarity 1: " + line1_similarity);
			if(line1_similarity > .6){
				footerList.add(footer1.getLine1());
				double line2_similarity = StringSimilarity.similarity(footer1.getLine2(), footer2.getLine2());
				System.out.println("===>>> Similarity 2: " + line2_similarity);
				if(line2_similarity > 0.6){
					footerList.add(footer1.getLine2());
					double line3_similarity = StringSimilarity.similarity(footer1.getLine3(), footer2.getLine3());
					System.out.println("===>>> Similarity 3: " + line3_similarity);
					if(line3_similarity > .6){
						footerList.add(footer1.getLine3());
					}
				}
			}
			
			System.out.println("===== Page Footer ======");
			System.out.println(">>>" + footerList + "<<<");
		}
		return footerList;
	}	
	
	private void setPossiblePageHeaderAndFooter(String pageContent, int pageNumber){
		String[] ss = pageContent.trim().split("\n");
		PossiblePageHeaderOrFooter header = new PossiblePageHeaderOrFooter();
		header.setPageNumber(pageNumber);
		for(int i = 0; i < ss.length; i++){
			if(i == 0){
				header.setLine1(ss[0]);
			}
			if(i == 1){
				header.setLine2(ss[1]);
			}
			if(i == 2){
				header.setLine3(ss[2]);
			}
		}
		this.possibleHeaderList.add(header);
		
		PossiblePageHeaderOrFooter footer = new PossiblePageHeaderOrFooter();	
		footer.setPageNumber(pageNumber);
		for(int i = ss.length -1; i >= 0; i--){
			if(i == ss.length -1){
				footer.setLine1(ss[i]);
			}
			if(i == ss.length -1 - 1 ){
				footer.setLine2(ss[i]);
			}
			if(i == ss.length -1 - 2){
				footer.setLine3(ss[i]);
			}
		}
		this.possibleFooterList.add(footer);
	}
	
	private void printPageInfo(){
		for(PageInfo pageInfo : pageList){
			System.out.println("=============================");
			System.out.println(pageInfo.pageNumber + ", " + pageInfo.numOfChars);
			//System.out.println(pageInfo.content);
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
				page = pageList.get(i).pageNumber;
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
				//sub.setContent(content);
				cList.add(sub);
			}

		}
		// at this point, we got all the categories and categories starting position
		// now let find the ending position
		
		// find endPosition, the endPositon is the startPositon of next Category
		if(cList.size() > 0){
			Category pre = cList.get(0);
			Category current = null;
			for(int i = 1; i < cList.size(); i++){
				current = cList.get(i);
				pre.setEndPosition(current.getStartPosition() - 1);
				pre = current;			
			}
			
			if(current == null){  // in this case, there is only one category
				pre.setEndPosition(content.length() + offset);
			}else{	// in this case, the current category is the last catey
				current.setEndPosition(content.length() + offset);
			}
		}
		this.setPageNumberInfoToCategory(cList);
		return cList;
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
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("/Users/zchen323/Downloads/AFNCRITSAlliantCorpsFinal.pdf"));
		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(is, ArticleTypePattern.PROPOSALS_2);
		
		List<Category> categoryList = info.getCategoryList();

		System.out.println(info.getTitle());
		//System.out.println(info.getContent());
		for(Category cat : categoryList){

			int start = cat.getStartPosition();
			int end = cat.getEndPosition();
			String catTitle = cat.getTitle();
			System.out.println(catTitle + " " + start + ", " + end);
			for(Category sub : cat.getSubCategory()){
				String subTitle = sub.getTitle();
				int sbstart = sub.getStartPosition();
				int sbend = sub.getEndPosition();
				System.out.println("\t" + subTitle + " " + sbstart + ", " + sbend);
			}			
		}	
	}	
}

class PageInfo {
	int pageNumber;
	String content;
	int numOfChars;
}
