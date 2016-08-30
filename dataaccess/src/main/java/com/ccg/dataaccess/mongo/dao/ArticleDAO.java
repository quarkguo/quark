package com.ccg.dataaccess.mongo.dao;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.ccg.ingestion.extract.ArticleInfo;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ArticleDAO {
	
	public void InsertArticle(ArticleInfo articleInfo){
		
		MongoClient mongoClient = MongoDbUtil.getMongoDbClient();
		MongoDatabase db = mongoClient.getDatabase("ccgcontent");
		MongoCollection<Document> collection = db.getCollection(articleInfo.getClass().getName());
		Document doc = new Document();

		
		Gson gson = new Gson();

		if(articleInfo.getId() == null){
			ObjectId objId = new ObjectId();
			articleInfo.setId(objId.toString());
			String articleInfoJson = gson.toJson(articleInfo);
			Object obj = com.mongodb.util.JSON.parse(articleInfoJson);			
			doc.put("_id", objId);
			doc.put(articleInfo.getClass().getSimpleName(), obj);
			collection.insertOne(doc);
		}else{
			doc.put("_id", articleInfo.getId());
			String articleInfoJson = gson.toJson(articleInfo);
			Object obj = com.mongodb.util.JSON.parse(articleInfoJson);
			doc.put(articleInfo.getClass().getSimpleName(), obj);
			collection.insertOne(doc);
		}			
		
	}
	
	public ArticleInfo findArticleById(String id){
		
		
		return null;
	}
	
	
	
	
}
