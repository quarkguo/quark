package com.ccg.common.file.util;

import java.io.File;

public class FileUtil {

	public static void deleteAll(File directory){
		
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteAll(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    directory.delete();
	}
}
