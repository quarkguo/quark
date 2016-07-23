package com.ccg.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XML {
	public static String toXml(Object obj) throws JAXBException{
		JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		Writer writer = new StringWriter();
		marshaller.marshal(obj, writer);
		return writer.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T>T fromXml(InputStream is, Class<T> type) throws JAXBException{
		JAXBContext ctx = JAXBContext.newInstance(type);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		T t = (T)unmarshaller.unmarshal(is);
		return t;
	}
	
	public static <T>T fromXml(String xml, Class<T> type) throws JAXBException, UnsupportedEncodingException{
		byte[] xmlBytes = xml.getBytes("UTF-8");
		InputStream is = new ByteArrayInputStream(xmlBytes);
		return fromXml(is, type);
	}
}
