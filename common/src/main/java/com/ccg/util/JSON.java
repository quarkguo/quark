package com.ccg.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {
	public static String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	
	public static <T>T fromJson(String json, Class<T> type){
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, type);
	}
}
