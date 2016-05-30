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
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGCategory;

//import lucene.demo.business.Hotel;
//import lucene.demo.business.HotelDatabase;  


/**
 *
 * @author John
 */
public class Indexer {
	
//	@Autowired
//	CCGDBService dataservice;
	
//	{
//	   	@SuppressWarnings("resource")
//		ApplicationContext context = 
//		      	  new ClassPathXmlApplicationContext(new String[]{
//		      			  "com/ccg/config/ccg_coreservices_beans.xml",
//		      			  "com/ccg/config/ccg_dataacess_beans_test.xml",
//		      			  "com/ccg/config/ccgrest-servlet.xml"
//		      	  });
//		dataservice = context.getBean(CCGDBService.class);		
//	}
	
	
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    private IndexWriter indexWriter = null;
    
    
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
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
    	
    	if(create){
    		File indexDirectory = new File(INDEX.DIRECTORY);
    		deleteDirectory(indexDirectory);
    	}
    	//indexWriter.deleteDocuments(arg0);
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File(INDEX.DIRECTORY));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }    
   
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
 
    public void indexArticle(CCGArticle article) throws IOException {
    	IndexWriter writer  = getIndexWriter(false);
    	this._indexArticle(article, writer);
    	this.closeIndexWriter();
    }
    
    
    public void _indexArticle(CCGArticle article, IndexWriter writer) throws IOException {
       	List<CCGCategory> catList = article.getCategorylist();
    	for(CCGCategory cat : catList){
    	       Document doc = new Document();
    	        doc.add(new StringField("cId", "" + cat.getCategoryID(), Field.Store.YES));
    	        doc.add(new StringField("cTitle", cat.getCategorytitle(), Field.Store.YES));
    	        doc.add(new StringField("aId", "" + article.getArticleID(), Field.Store.YES));    	        
    	        doc.add(new StringField("aTitle", article.getTitle(), Field.Store.YES));
    	        
    	        String fullSearchableText = cat.getCategorytitle() + " " +  cat.getCategorycontent();
    	        doc.add(new TextField(INDEX.CONTENT, fullSearchableText, Field.Store.NO));
    	        writer.addDocument(doc);   		
    	}
    }   
    
    public void rebuildIndexes(List<CCGArticle> ccgArticleList) throws IOException {
          //
          // Erase existing index
          //
    	IndexWriter writer = getIndexWriter(true);
       
          for(CCGArticle article : ccgArticleList){
        	  this._indexArticle(article, writer);
          }
          //
          // Don't forget to close the index writer when done
          //
          closeIndexWriter();
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
