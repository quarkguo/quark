package com.ccg.services.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.ccg.common.data.SearchResult;
import com.ccg.common.data.SearchResult2;
import com.ccg.util.ConfigurationManager;
import com.ccg.util.JSON;



public class SearchEngine {
	private IndexSearcher searcher = null;
	private QueryParser parser = null;
	private String indexLocation = ".";

	/** Creates a new instance of SearchEngine */
	public SearchEngine() throws IOException {
		Properties prop = ConfigurationManager.getConfig("ccg.properties");
		indexLocation = prop.getProperty("index.repository");
		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexLocation))));
		parser = new QueryParser(INDEX.CONTENT, new StandardAnalyzer());
	}

	public TopDocs performSearch(String queryString, int n) throws IOException, ParseException {
		Query query = parser.parse(queryString);
		return searcher.search(query, n);
	}

	public Document getDocument(int docId) throws IOException {
		return searcher.doc(docId);
	}
	
	public List<SearchResult> search(String queryString, int n) throws IOException, ParseException{
		List<SearchResult> result = new ArrayList<SearchResult>();
		
		Query query = parser.parse(queryString);
		TopDocs topDocs = searcher.search(query, n);
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = 0; i < hits.length; i++) {
	            Document doc = getDocument(hits[i].doc);
	            SearchResult sr = new SearchResult();
	            sr.setCategoryId(doc.get("cId"));
	            sr.setCategoryTitle(doc.get("cTitle"));
	            sr.setArticleTitle(doc.get("aTitle"));
	            sr.setArticleId(doc.get("aId"));
	            sr.setScore(hits[i].score);
	            String t=sr.getArticleTitle();
	            if(t.length()>18)
	            {
	            	t=t.substring(0,15)+"...";
	            }
	            sr.setIndexText("Article:["+sr.getArticleId()+"]-"+t+"--Category:["+sr.getCategoryId()+"]");
	            
	            System.out.println("++++" + JSON.toJson(sr));
	            result.add(sr);
	        }
	    return result;
	}
	
	public List<SearchResult2> search2(String queryString, int n) throws IOException, ParseException{
		List<SearchResult2> result = new ArrayList<SearchResult2>();
		
		Query query = parser.parse(queryString);
		TopDocs topDocs = searcher.search(query, n);
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = 0; i < hits.length; i++) {
	            Document doc = getDocument(hits[i].doc);
	            SearchResult2 sr = new SearchResult2();
	            List<IndexableField> fields=doc.getFields();

	            sr.setArticleId(doc.get("aId"));
	            sr.setArticleTitle(doc.get("aTitle"));
	            sr.setPageNumber(doc.get("aPageNum"));
	            sr.setScore(hits[i].score);
	            result.add(sr);
	        }
	 //   sortSearchResult(result);
	    return result;
	}
	
	public HashMap<Integer,List<Integer>> sortSearchResult(List<SearchResult2> data)
	{
		HashMap<Integer,List<Integer>> res=new HashMap<Integer,List<Integer>>();
		for(SearchResult2 s:data)
		{
			Integer articleID=Integer.parseInt(s.getArticleId());
			Integer pagenum=Integer.parseInt(s.getPageNumber());
			
			if(res.containsKey(articleID))
			{
				List<Integer> pages=res.get(articleID);
				pages.add(pagenum);
			}
			else
			{
				List<Integer> pages=new ArrayList<Integer>();
				pages.add(pagenum);
				res.put(articleID, pages);
			}
		}
		
		for(Integer k:res.keySet())
		{
			System.out.println("^^^^^^ artcile:"+k+"  pages :"+res.get(k).size());
		}
		return res;
	}
		
	
	
	
}
