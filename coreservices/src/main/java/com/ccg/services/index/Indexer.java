/*
 * Indexer.java
 *
 * Created on 6 March 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.ccg.services.index;

import java.io.File;
import java.io.IOException;
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
//import org.apache.lucene.store.jdbc.JdbcDirectory;
//import org.apache.lucene.store.jdbc.dialect.MySQLDialect;
import org.apache.lucene.util.Version;

import com.ccg.util.ConfigurationManager;

public class Indexer {
	
	private String indexLocation = ".";
	private String metaIndexLocation = "./meta";
	
    /** Creates a new instance of Indexer */
    public Indexer() {
    	Properties prop = ConfigurationManager.getConfig("ccg.properties");
    	indexLocation = prop.getProperty("index.repository");
    	metaIndexLocation = prop.getProperty("metaindex.repository");
    	
    	System.out.println("===Index Location: " + indexLocation);
    	System.out.println("===MetaIndex Location: " + metaIndexLocation);    
    }
 
    private IndexWriter indexWriter = null;
    private IndexWriter metaIndexWriter = null;
    
    
//    public IndexWriter getJdbcIndexWriter(boolean create) throws IOException{
//    	// http://stackoverflow.com/questions/14515575/exception-while-storing-lucene-index-in-db
//    	// http://kalanir.blogspot.com/2008/06/creating-search-index-in-database.html
//    	
//    	//code snippet to create index  
//    	MysqlDataSource dataSource = new MysqlDataSource();  
//    	//dataSource.setDatabaseName(dbName);  
//    	dataSource.setUser("root");  
//    	  
//    	dataSource.setPassword("password");  
//    	  
//    	dataSource.setDatabaseName("test");  
//    	  
//    	dataSource.setEmulateLocators(true); //This is important because we are dealing with a blob type data field  
//    	  
//    	JdbcDirectory jdbcDir = new JdbcDirectory(dataSource, new MySQLDialect(), "indexTable");  
//    	  
//    	jdbcDir.create(); // creates the indexTable in the DB (test). No need to create it manually    	
//        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
//        indexWriter = new IndexWriter(jdbcDir, config);   	
//    	
//    	return indexWriter;
//    }
    
    public IndexWriter getIndexWriter(boolean create) {
    	
    	if(create){
    		File indexDirectory = new File(indexLocation);
    		System.out.println("++++++ delete: " + indexDirectory.getAbsolutePath());
    		deleteDirectory(indexDirectory);
    	}
    	//indexWriter.deleteDocuments(arg0);
        if (indexWriter == null) {
            Directory indexDir = null;
			try {
				indexDir = FSDirectory.open(new File(indexLocation));
				System.out.println("====>>> " + indexDir.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
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
        }
        return indexWriter;
   }    
 
    public IndexWriter getMetaIndexWriter(boolean create) {
    	
    	if(create){
    		File metaIndexDirectory = new File(metaIndexLocation);
    		System.out.println("++++++ delete: " + metaIndexDirectory.getAbsolutePath());
    		deleteDirectory(metaIndexDirectory);
    	}
    	//indexWriter.deleteDocuments(arg0);
        if (metaIndexWriter == null) {
            Directory metaIndexDir = null;
			try {
				metaIndexDir = FSDirectory.open(new File(metaIndexLocation));
				System.out.println("====>>> " + metaIndexDir.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            try {
				metaIndexWriter = new IndexWriter(metaIndexDir, config);
			} catch (IOException e) {
				e.printStackTrace();
				if(metaIndexWriter != null){
					try {
						metaIndexWriter.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
        }
        return metaIndexWriter;
   }    
   
    public void closeIndexWriter() {
        if (indexWriter != null) {
            try {
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
   }
    public void closeMetaIndexWriter() {
        if (metaIndexWriter != null) {
            try {
				metaIndexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
   }

    
 	public void indexingCategory(String catId, String catTitle, String catContent,
 			String articleId, String articleTitle, IndexWriter writer){
    	
 		//IndexWriter writer  = getIndexWriter(false);
 		
    	
 		try {
 	       Document doc = new Document();
	        doc.add(new StringField("cId", "" + catId, Field.Store.YES));
	        doc.add(new StringField("cTitle", catTitle, Field.Store.YES));
	        doc.add(new StringField("aId", "" + articleId, Field.Store.YES));    	        
	        doc.add(new StringField("aTitle", articleTitle, Field.Store.YES));
	        
	        String fullSearchableText = catTitle + " " +  catContent;
	        doc.add(new TextField(INDEX.CONTENT, fullSearchableText, Field.Store.NO));
	        writer.addDocument(doc);
	        
		} catch (IOException e) {
			e.printStackTrace();
		} 		
 	}
     
 	public void indexingPage(String articleId, String articleTitle, String pageNumber,
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
 	
 	public void indexingMetadata(String articleId, String articleTitle, String lastupdatetime, String metadata, IndexWriter writer){
		try {
	 	       Document doc = new Document();
		        doc.add(new StringField("aId", articleId, Field.Store.YES));
		        doc.add(new StringField("aTitle", articleTitle, Field.Store.YES));
		        doc.add(new StringField("aPageNum", lastupdatetime, Field.Store.YES));    	        
		        
		        String fullSearchableText = metadata;
		        doc.add(new TextField(INDEX.CONTENT, fullSearchableText, Field.Store.NO));
		        writer.addDocument(doc);
		        
			} catch (IOException e) {
				e.printStackTrace();
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
}