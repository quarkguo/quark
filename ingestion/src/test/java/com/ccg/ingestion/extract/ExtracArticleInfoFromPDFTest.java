package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.ccg.ingestion.extract.ArticleInfo;
import com.ccg.ingestion.extract.ArticleTypePattern;
import com.ccg.ingestion.extract.Category;
import com.ccg.ingestion.extract.ExtractArticleInfo;

public class ExtracArticleInfoFromPDFTest {
	
	@Test
	public void testExtractArticleInfoFromPDF() throws IOException{

		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx (1).pdf"));
		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(is, ArticleTypePattern.PROPOSALS_1);
		System.out.println("Title: " + info.getTitle());
		System.out.println("File Type: " + info.getType());
		System.out.println("Num of Pages: " + info.getPages());
		
		for(Category c : info.getCategoryList()){
			System.out.println(c.getTitle() + ", " 
					+ c.getStartPosition() + ", " + c.getEndPosition() + ", p"
					+ c.getStartPage() + ", p" + c.getEndPage());
			if(c.getSubCategory().size() > 0){
				for(Category sub : c.getSubCategory()){
					System.out.println(sub.getTitle() + ", " 
							+ sub.getStartPosition() + ", " + sub.getEndPosition() + ", p"
							+ sub.getStartPage() + ", p" + sub.getEndPage());
				}
			}
		}
		//System.out.println(
		//info.getContent().substring(8188, 11996));
	}

}
