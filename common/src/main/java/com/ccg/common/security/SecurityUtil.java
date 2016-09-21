package com.ccg.common.security;

import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.Base64;

public class SecurityUtil {

	public static String base64Encode(byte[] data){
		String b64s = Base64.getEncoder().encodeToString(data);		
		int leng = b64s.length();
		int current = 0;
		StringBuffer sb = new StringBuffer();
		while(current < leng){
			if(current % 77 == 0){
				sb.append("\n");
			}
			sb.append(b64s.charAt(current));		
			current++;
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public static byte[] base64Decode(String base64String){
		base64String = base64String.replaceAll("\\s+", "");
		return Base64.getDecoder().decode(base64String);		
	}
	
	
	
	public static byte[] sign(PrivateKey key, byte[] data) throws CCGSecurityException{	
		try{
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign(key);
			signature.update(data);
			return signature.sign();
		}catch(Exception e){
			throw new CCGSecurityException(e);
		}
	}
	
	public static boolean verify(PublicKey key, byte[] data, byte[] sig) throws CCGSecurityException {
		try{
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initVerify(key);
			signature.update(data);
			return signature.verify(sig);
		}catch(Exception e){
			throw new CCGSecurityException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		
		String data = "Hello World";
		
		  System.out.println(System.getProperty("java.version"));
		  for (Provider provider : Security.getProviders())
		    System.out.println("====>>>>" + provider);
		
		byte[] sig = SecurityUtil.sign(MyPrivateKey.getKey(), data.getBytes());
		
		System.out.println(SecurityUtil.base64Encode(sig));
		
		boolean b = SecurityUtil.verify(MyPublicKey.getKey(), "Hello Wrold".getBytes(), sig);
		
		System.out.println(b);
	}
	
}
