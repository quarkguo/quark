package com.ccg.dataaccess.mongo.dao;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDbUtil {

	
	public static MongoClient getMongoDbClient(){
		
		return new MongoClient("localhost", 27017);
	}
	
	
	/*
	// get connection
	//MongoClient mongoClient = new MongoClient("92.168.0.102", 27017);
	
	// MongoClient mongoClient = new MongoClient("localhost");
	 MongoClient mongoClient = new MongoClient("localhost", 27017 );
	// MongoClient mongoClient2 = new MongoClient(new ServerAddress("db.server.com", 21070));
	
	
	// get databse
	MongoDatabase db = mongoClient.getDatabase("test");
	
	// get collection (table)
	MongoCollection<Document> collection = db.getCollection("employee");
	
	System.out.println("collection: " + collection);
	
	// inserts
	System.out.println("Inserting using BasicDBObjects...");
	BasicDBObject basic1 = new BasicDBObject();
	
	*/
}

