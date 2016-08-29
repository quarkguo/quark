package com.ccg.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {

	public static String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	
	public static <T>T fromJson(String json, Class<T> type){
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, type);
	}
	
	public static int countWord(String content, String token)
	{
		int count=0;
		int posi=0;
		String lcontent=content.toLowerCase();
		String ltoken=token.toLowerCase();
		while(posi != -1){

		    posi = lcontent.indexOf(ltoken,posi);

		    if(posi != -1){
		        count ++;
		        posi += ltoken.length();
		    }
		}
		return count;
	}
}
