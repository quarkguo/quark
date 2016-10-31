package com.ccg.services.index2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ccg.common.file.util.FileUtil;
import com.ccg.services.index.INDEX;
import com.ccg.util.ConfigurationManager;

public class Indexer {
	
	private IndexWriter articleIndexWriter;
	private IndexWriter articleMetaIndexWriter;
	private static String indexRoot = ".";
	
	/*
	 	index directory structure
	 		indexRoot--
	 		          |_ Article_Category----
	 		          |                     |_ Article_Index
	 		          |                     |_ Article_Meta_Index
	 		          |
	 		          |_ Article2_Category--|_ Article_Index
	 		                                |_ Article_Meta_Index	 		                                                    			
	 */	
	static {
    	Properties prop = ConfigurationManager.getConfig("ccg.properties");
    	indexRoot = prop.getProperty("index.repository");	
    	System.out.println("====== indexRoot: " + indexRoot);
	}
	
	public IndexWriter getArticleWriter(String articleCategoryName, boolean create){
		String indexLocation = getArticleIndexLocation(articleCategoryName);
		if(create){
			File indexDirectory = new File(indexLocation);
			deleteDirectory(indexDirectory);
		}
		
		if(articleIndexWriter == null){
			articleIndexWriter = getIndexWriter(indexLocation);
		}
		
		return articleIndexWriter;
	}
	
	public IndexWriter getArticleMetaWriter(String articleCategoryName, boolean create){
		String indexLocation = getArticleMetaDataIndexLocation(articleCategoryName);
		if(create){
			File indexDirectory = new File(indexLocation);
			deleteDirectory(indexDirectory);
		}
		
		if(articleMetaIndexWriter == null){
			articleMetaIndexWriter = getIndexWriter(indexLocation);
		}
		
		return articleMetaIndexWriter;
	}
	
 	public void indexing(String articleId, String articleTitle, String pageNumber,
 			String pageContent, IndexWriter writer){
		try {
	 	       Document doc = new Document();
		        doc.add(new StringField("aId", articleId, Field.Store.YES));
		        doc.add(new StringField("aTitle", articleTitle, Field.Store.YES));
		        doc.add(new StringField("aPageNum", pageNumber, Field.Store.YES));    	        
		        
		        String fullSearchableText = pageContent;
		        doc.add(new TextField(INDEX.CONTENT, fullSearchableText, Field.Store.NO));
		        writer.addDocument(doc);
		        
			} catch (IOException e) {
				e.printStackTrace();
			} 					
 	}	
	
	
	@SuppressWarnings("unused")
	private IndexWriter getIndexWriter(String indexLocation){

        Directory indexDir = null;
		try {
			indexDir = FSDirectory.open(new File(indexLocation));
			System.out.println("====>>> " + indexDir.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
        IndexWriter indexWriter = null;
        try {
			indexWriter = new IndexWriter(indexDir, config);
		} catch (IOException e) {
			e.printStackTrace();
			if(indexWriter != null){
				try {
					indexWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return indexWriter;
	}

	
    public void closeArticleIndexWriter() {
        if (articleIndexWriter != null) {
            try {
				articleIndexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
   }

    public void closeArticleMetaIndexWriter() {
        if (articleMetaIndexWriter != null) {
            try {
				articleMetaIndexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
   }
    
    
	
    private boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
   }
   
   public static void deleteArticleIndex(String articleCategoryName){
	   File indexDir = new File(indexRoot + File.separator + articleCategoryName + File.separator + "articleIndex");
	   FileUtil.deleteAll(indexDir);
   } 
   
   public static void deleteArticleMetaIndex(String articleCategoryName){
	   File indexDir = new File(indexRoot + File.separator + articleCategoryName + File.separator + "articleMataIndex");
	   FileUtil.deleteAll(indexDir);	   
   }
    
   public static void deleteAllArticleMetaIndex(){	   
	   File directory = new File(indexRoot);	   
	   File[] files = directory.listFiles();
	   for(File f : files){
		   if(f.isDirectory()){
			   deleteArticleMetaIndex(f.getName());
		   }		   
	   }
   }

   public static void deleteAllArticleIndex(){	   
	   File directory = new File(indexRoot);	   
	   File[] files = directory.listFiles();
	   for(File f : files){
		   if(f.isDirectory()){
			   deleteArticleIndex(f.getName());
		   }		   
	   }
   }   
   
   public static String getArticleIndexLocation(String articleCategoryName){
	   if(articleCategoryName == null || articleCategoryName.trim().length() == 0){
		   articleCategoryName = "default";
	   }
		String indexLocation = 
				indexRoot + File.separator
				+ articleCategoryName.replace(' ', '_') + File.separator + 
				"articleIndex";
	   return indexLocation;
   }
   
   public static String getArticleMetaDataIndexLocation(String articleCategoryName){
	   if(articleCategoryName == null || articleCategoryName.trim().length() == 0){
		   articleCategoryName =  "default";
	   }
		String indexLocation = 
				indexRoot + File.separator 
				+ articleCategoryName.replace(' ',  '_') + File.separator + 
				"articleMataIndex";
	   return indexLocation;
   }  
}
