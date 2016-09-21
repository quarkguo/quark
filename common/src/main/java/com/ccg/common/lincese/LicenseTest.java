package com.ccg.common.lincese;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.ccg.util.ConfigurationManager;
import com.ccg.util.XML;

public class LicenseTest {
	public static void main(String[] args) throws InvalidLicenseException {
		
		try{
			createLicense();
			validLicense();
			
		}catch(LicenseExpiredException e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void validLicense() throws LicenseExpiredException, InvalidLicenseException {
		License license = ConfigurationManager.getConfig(License.class, "/Users/zchen323/license.xml");
		LicenseUtil.varify(license);
		System.out.println("valid license");
	}
	
	public static void createLicense() {
		// create a license
		License license = new License();
		license.setClientName("new client");
		
		// license expired after one yest
		Calendar c = Calendar.getInstance();
		//c.add(Calendar.YEAR, 1);
		c.add(Calendar.DATE, -1);
		license.setGoodBeforeDate(new Date(c.getTimeInMillis()));
		
		license.setIssuedBy("ccg");
		license.setIssuedDate(new Date());
		license.getModule().add("Module-1");
		license.getModule().add("Module-2");
		
		license.setNumberOfUsers(2);
		license.setProductName("Document Management");
		license.setProductKey("1234-5678-9000");
		try{
			LicenseUtil.sign(license);
			
			// outout to a file
			FileOutputStream fos = new FileOutputStream(new File("/Users/zchen323/license.xml"));
			fos.write(XML.toXml(license).getBytes());
			fos.flush();
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
