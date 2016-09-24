package com.ccg.common.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * private key will be loaded from file system or keystore.
 * @author zchen323
 *
 */
public class MyPrivateKey {
	
	public static byte[] getKeyBytes(){		
		InputStream is = ClassLoader.class.getResourceAsStream("/myPrivateKey.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while((line = br.readLine()) != null){
				sb.append(line);			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Base64.getDecoder().decode(sb.toString());
	}
	
	public static PrivateKey getKey() throws NoSuchAlgorithmException, InvalidKeySpecException{
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(getKeyBytes());
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(privateKeySpec);
		return  privateKey;
	}	
	
	/*
	To use the code, you need corresponding public and private RSA keys. 
	RSA keys can be generated using the open source tool OpenSSL. 
	However, you have to be careful to generate them in the format 
	required by the Java encryption libraries. 
	To generate a private key of length 2048 bits:

		openssl genrsa -out private.pem 2048

	To get it into the required (PKCS#8, DER) format:

		openssl pkcs8 -topk8 -in private.pem -outform DER -out private.der -nocrypt

	To generate a public key from the private key:

		openssl rsa -in private.pem -pubout -outform DER -out public.der

		Hard code public key into code. Private key is in file system.
	*/	
}
