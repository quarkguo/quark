package com.ccg.common.lincese;

import com.ccg.util.ConfigurationManager;
import com.ccg.util.XML;

public class LicenseTool {
	
	public static void main(String[] args)throws Exception{
		License license = ConfigurationManager.getConfig(License.class, "/Users/zchen323/testLicense.xml");
		LicenseUtil.sign(license);
		System.out.println(XML.toXml(license));
	}
}
