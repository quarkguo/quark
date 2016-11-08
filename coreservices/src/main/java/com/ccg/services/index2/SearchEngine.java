package com.ccg.services.index2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.ccg.common.data.SearchResult2;
import com.ccg.util.ConfigurationManager;

public class SearchEngine {
	
	private static String indexRoot = ".";
	
	static {
		Properties prop = ConfigurationManager.getConfig("ccg.properties");
		indexRoot = prop.getProperty("index.repository");		
	}
	
	public static List<SearchResult2> searchArticle(String queryString, int limit, String[] types) throws IOException, ParseException{	
		List<SearchResult2> result = new ArrayList<SearchResult2>();		
		if(types != null && types.length > 0){
			for(String articleType : types){
				articleType = articleType.trim();
				articleType = articleType.replace(' ', '_');				
				result.addAll(searchArticle(articleType, queryString, limit));
			}
		}else{			
			File directory = new File(indexRoot);	   
			File[] files = directory.listFiles();
			for(File f : files){
			   if(f.isDirectory()){
				   result.addAll(searchArticle(f.getName(), queryString, limit));
			   }		   
			}
		}
		return result;
	}

	public static List<SearchResult2> searchArticleMetaData(String queryString, int limit, String[] types) throws IOException, ParseException{	
		List<SearchResult2> result = new ArrayList<SearchResult2>();
		if(types != null && types.length > 0){
			for(String articleType : types){
				articleType = articleType.trim();
				articleType = articleType.replace(' ', '_');
				result.addAll(searchArticleMetaData(articleType, queryString, limit));
			}
		}else{
			File directory = new File(indexRoot);	   
			File[] files = directory.listFiles();
			for(File f : files){
			   if(f.isDirectory()){
				   result.addAll(searchArticleMetaData(f.getName(), queryString, limit));
			   }		   
			}
		}
		return result;
	}
	
	
	public static List<SearchResult2> searchArticle(String articleCategoryName, String queryString, int limit) throws ParseException{
		
		List<SearchResult2> result = new ArrayList<SearchResult2>();
		
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		Query query = parser.parse(queryString);
		try{
			IndexSearcher searcher = getArticleIndexSearcher(articleCategoryName);
			
			TopDocs topDocs = searcher.search(query, limit);
		    
			ScoreDoc[] hits = topDocs.scoreDocs;
		    for (int i = 0; i < hits.length; i++) {
		            Document doc = searcher.doc(hits[i].doc);
		            SearchResult2 sr = new SearchResult2();
		            sr.setArticleId(doc.get("aId"));
		            sr.setArticleTitle(doc.get("aTitle"));
		            sr.setPageNumber(doc.get("aPageNum"));	            
		            sr.setScore(hits[i].score);
		            sr.setArticleType(articleCategoryName);
		            result.add(sr);
		    }
		}catch(IOException e){
			e.printStackTrace();
		}
	    return result;		
	}
	
	public static List<SearchResult2> searchArticleMetaData(String articleCategoryName, String queryString, int limit) throws ParseException{
		
		List<SearchResult2> result = new ArrayList<SearchResult2>();
		Map<String, SearchResult2> map = new HashMap<String, SearchResult2>();

		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		Query query = parser.parse(queryString);
		try{
			IndexSearcher searcher = getArticleMetaDataIndexSearcher(articleCategoryName);
			
			TopDocs topDocs = searcher.search(query, limit);
			
			ScoreDoc[] hits = topDocs.scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				SearchResult2 sr = new SearchResult2();
	
				sr.setArticleId(doc.get("aId"));
				sr.setArticleTitle(doc.get("aTitle"));
				sr.setPageNumber(doc.get("aPageNum"));
				sr.setArticleType(articleCategoryName);
	
				sr.setScore(hits[i].score);
				if(map.containsKey(sr.getArticleId())){
					SearchResult2 temp = map.get(sr.getArticleId());
					if(sr.getPageNumber().compareTo(temp.getPageNumber()) > 0){
						map.put(sr.getArticleId(), sr);
					}				
				}else{
					map.put(sr.getArticleId(), sr);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			
		}
		Set<String> keys = map.keySet();
		for(String key : keys){
			SearchResult2 temp = map.get(key);
			// set page number to -1, indicate metadata
			temp.setPageNumber("-1");
			result.add(temp);
		}
		
		// sort by score
		Collections.sort(result, new Comparator<SearchResult2>(){

			@Override
			public int compare(SearchResult2 o1, SearchResult2 o2) {
				if(o1.getScore() - o2.getScore() > 0)
					return -1;
				else if(o1.getScore() - o2.getScore() == 0)
					return 0;
				else
					return 1;
			}			
		});
		return result;
	}	
	
	private static IndexSearcher getArticleIndexSearcher(String articleCategoryName) throws IOException{
		String indexLocation = Indexer.getArticleIndexLocation(articleCategoryName);
		
		System.out.println("=========>>>> indexLocation: " + indexLocation);
		
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexLocation))));
		return searcher;
	}
	
	private static IndexSearcher getArticleMetaDataIndexSearcher(String articleCategoryName) throws IOException{
		String indexLocation = Indexer.getArticleMetaDataIndexLocation(articleCategoryName);
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexLocation))));
		return searcher;
	}
}
