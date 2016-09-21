package com.ccg.common.lincese;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import com.ccg.common.security.CCGSecurityException;
import com.ccg.common.security.MyPrivateKey;
import com.ccg.common.security.MyPublicKey;
import com.ccg.common.security.SecurityUtil;

public class LicenseUtil {
	
	/**
	 * Use private key to sign the license
	 * 
	 * @param license
	 */
	public static void sign(License license) throws CCGSecurityException {
		try{
			PrivateKey privateKey = MyPrivateKey.getKey();
			byte[] sig = SecurityUtil.sign(privateKey, license.toString().getBytes());
			license.setSignature(SecurityUtil.base64Encode(sig));
		}catch(Exception e){
			throw new CCGSecurityException(e);
		}
	}
	
	public static void varify(License license) throws LicenseExpiredException, InvalidLicenseException{
		try{
			byte[] sig = SecurityUtil.base64Decode(license.getSignature());
			PublicKey publicKey = MyPublicKey.getKey();
			if(!SecurityUtil.verify(publicKey, license.toString().getBytes(), sig)){
				throw new InvalidLicenseException("Invalid License!");
			}
			Date goodBefore = license.getGoodBeforeDate();
			Date now = new Date();
			if(goodBefore.getTime() - now.getTime() < 0){
				DateAdapter dateAdapter = new DateAdapter();
				throw new LicenseExpiredException("License Expired: this license expored on " + 
						dateAdapter.marshal(license.getGoodBeforeDate()));
			}
		}catch(CCGSecurityException e){
			throw new RuntimeException(e);
		}
		
	}
}
