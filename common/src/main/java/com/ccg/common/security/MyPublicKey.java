package com.ccg.common.security;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
public class MyPublicKey {
	private static String key=""+
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwc2toOHj2B4Hfg6iuhqlFXHifGkEepiWS"+
			"QHhmsi9nZ3sO7cfKxKwcV9A3UEjUwNbZv0l9L1JHre+XwyykZuTlLCstKrtwxyZoFlt3KZKxNMO5k"+
			"h46st0NUr2I/1xVZXSk2HZpQHGQ4Lc6KZIJC/8VaZ4TTJcOfeFkhU4CVQuHjwVfijlMHdtNT8Pokr"+
			"ilXSr951ET4GAdRBX7I4IURdzwk4kBImT6ZYfEoZ39lguJbYvGtqqg9obGUvf0Bfpqy6K3Tw4wmYI"+
			"mXOs3oFlZUiush9CrDxGwoa2UBVYh5fFP3ScK/aRnIoUvyqbsxgD4pO+fUQdKFoEtpcF6+2B9aMTE"+
			"wIDAQAB";
	
	public static byte[] getKeyBytes(){
		return Base64.getDecoder().decode(key);
	}
	
	public static PublicKey getKey() throws SecurityException {
		try{
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(getKeyBytes());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey publicKey = kf.generatePublic(publicKeySpec);
			return publicKey;
		}catch(Exception e){
			throw new SecurityException(e);
		}
	}	
}
