package com.ccg.common.lincese;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlType(propOrder = { "clientName", "productName", "productKey", "goodBeforeDate", "numberOfUsers", "module",
		"issuedBy", "issuedDate", "signature" })

public class License {
	private String clientName;
	private String productName;
	private String productKey;
	private Date goodBeforeDate; // format: 2016-01-31
	private int numberOfUsers;
	private List<String> module;
	private String issuedBy;
	private Date issuedDate = new Date();
	private String signature;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getGoodBeforeDate() {
		return goodBeforeDate;
	}

	public void setGoodBeforeDate(Date goodBeforeDate) {
		this.goodBeforeDate = goodBeforeDate;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	@XmlElementWrapper(name = "modules")
	public List<String> getModule() {
		if (module == null) {
			module = new ArrayList<String>();
		}
		return module;
	}

	public void setModule(List<String> module) {
		this.module = module;
	}

	public String toString() {
		//////////////////////////////////////////////////////////////////////////////
		// //
		// IMPORTANT: DON'T CHANGE THE ORDER OF ATTRIBUTE IN toString() METHOD.
		////////////////////////////////////////////////////////////////////////////// //
		// //
		//////////////////////////////////////////////////////////////////////////////
		DateAdapter dateAdapter = new DateAdapter();
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(this.getClientName());
			sb.append(this.getProductName());
			sb.append(this.getProductKey());
			sb.append(dateAdapter.marshal(this.getGoodBeforeDate()));
			sb.append(this.getIssuedBy());
			sb.append(dateAdapter.marshal(this.getIssuedDate()));
			sb.append(this.getNumberOfUsers());
			// modules
			StringBuffer modules = new StringBuffer();
			for (String m : this.getModule()) {
				sb.append(m);
			}
			sb.append(modules.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}

class DateAdapter extends XmlAdapter<String, Date> {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String marshal(Date v)  {
		synchronized (dateFormat) {
			return dateFormat.format(v);
		}
	}

	@Override
	public Date unmarshal(String v) throws ParseException {
		synchronized (dateFormat) {
			return dateFormat.parse(v);
		}
	}

}
