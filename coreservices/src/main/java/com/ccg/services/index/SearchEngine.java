/*
 * SearchEngine.java
 *
 * Created on 6 March 2006, 14:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.ccg.services.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.ccg.common.data.SearchResult;
import com.ccg.util.JSON;


/**
 *
 * @author John
 */
public class SearchEngine {
	private IndexSearcher searcher = null;
	private QueryParser parser = null;

	/** Creates a new instance of SearchEngine */
	public SearchEngine() throws IOException {
		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(INDEX.DIRECTORY))));
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
	            
	            System.out.println("++++" + JSON.toJson(sr));
	            
	            result.add(sr);
	        }
	    return result;
	}
	
}
