package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import con.ccg.ingestion.extract.ArticleInfo;
import con.ccg.ingestion.extract.ArticleTypePattern;
import con.ccg.ingestion.extract.Category;
import con.ccg.ingestion.extract.ExtractArticleInfo;

public class ExtracArticleInfoFromPDFTest {
	
	@Test
	public void testExtractArticleInfoFromPDF() throws IOException{

		InputStream is = new FileInputStream(
				new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(is, ArticleTypePattern.PROPOSALS);
		System.out.println("Title: " + info.getTitle());
		System.out.println("File Type: " + info.getType());
		System.out.println("Num of Pages: " + info.getPages());
		
		for(Category c : info.getCategoryList()){
			System.out.println(c.getTitle() + ", " + c.getStartPosition() + ", " + c.getEndPosition());
			if(c.getSubCategory().size() > 0){
				for(Category sub : c.getSubCategory()){
					System.out.println(sub.getTitle() + ", " + sub.getStartPosition() + ", " + sub.getEndPosition());
				}
			}
		}
	}

}
