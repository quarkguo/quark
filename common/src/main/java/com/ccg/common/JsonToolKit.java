package com.ccg.common;

import java.util.*;

// this toolkit does not incoorporate the URL encoding for data
// so in some scenario the xml toolkit should be more useful if client side parse is not an issue

public class JsonToolKit {	

	public static JsonToolKit getToolKit()
	{
		return new JsonToolKit();
	}
	
	public String toJSON(Integer data)
	{
		return data.toString();
	}
	
	public String toJSON(int[] data)
	{
		StringBuffer buf=new StringBuffer();
		buf.append('[');
		for(int i=0;i<data.length;i++)			
		{
			 if(i!=0) buf.append(',');
			 buf.append(toJSON(data[i]));
		}
		buf.append(']');
		return new String(buf);	
	}
	public String toJSON(Double data)
	{
		return data+"";
	}
	public String toJSON(int data)
	{
		 return data+"";
	}
	public String toJSON(boolean data)
	{
		return data+"";
	}
	public String toJSON(long data)
	{
		 return data+"";
	}
	
	
	 public String toJSON(String data)
	{
		 String escapedata=data.replace("\"", "\\\"");
		 StringBuffer buf=new StringBuffer();
		 buf.append('"');
		 buf.append(escapedata);
		 buf.append('"');
		 return new String(buf);
	}
	
	 public String toJSON(List<Object> data)
	{
		StringBuffer buf=new StringBuffer();
		buf.append('[');
		for(int i=0;i<data.size();i++)
		{
			 if(i!=0) buf.append(',');
			 buf.append(toJSON(data.get(i)));
		}
		buf.append(']');
		return new String(buf);
	}
	
	 public String toJSON(Object[] data)
	{
		StringBuffer buf=new StringBuffer();
		buf.append('[');
		for(int i=0;i<data.length;i++)			
		{
			 if(i!=0) buf.append(',');
			 buf.append(toJSON(data[i]));
		}
		buf.append(']');
		return new String(buf);
	}
	
	// here we all keys are string
	 public String toJSON(Map<String,Object> map)
	{
	   	StringBuffer buf=new StringBuffer();	
	   	buf.append('{');
	   	Iterator<String> it=map.keySet().iterator();
	   	boolean isFirst=true;
	   	while (it.hasNext())
	   	{
	   	   Object key=it.next();
	   	   if(!isFirst) buf.append(',');
	   	//   buf.append('"');
	   	   buf.append("\""+key+"\"");
	   	//   buf.append('"');
	   	   buf.append(':');
	   	   buf.append(toJSON(map.get(key)));
	   	   isFirst=false;	
	   	}
	   	buf.append('}');
	   	return new String(buf);
	}
	
	public String toJSON(Set data) 
	{
		StringBuffer buf=new StringBuffer();	
	   	buf.append('[');
	   	Iterator it=data.iterator();
	   	boolean isFirst=true;
	   	while (it.hasNext())
	   	{
	   	 if(!isFirst) buf.append(",");
	   	   buf.append(toJSON(it.next()));
	   	   isFirst=false;	
	   	}
	   	buf.append(']');
	   	return new String(buf);
	}
	public String toJSON(Object data) 
	{
		if(data==null)
		{
			return null;
		}
		if(data instanceof Integer)
		{
			return toJSON(((Integer)data).intValue());
		}
		else if(data instanceof Boolean)
		{
			return toJSON(((Boolean)data).booleanValue());
		}
		else if(data instanceof Long)
		{
			return toJSON(((Long)data).longValue());
		}
		else if(data instanceof JsonDTO)
		{
			return ((JsonDTO)data).getJSON();
		}
		else if(data instanceof BaseDTO)
		{
			return JsonToolKit.getToolKit().toJSON(((BaseDTO)data).getDataMap());
		}
		else if(data instanceof String)
		{
			return toJSON((String)data);
		}
		else if(data instanceof Object[])
		{
			return toJSON((Object[])data);
		}
		else if(data instanceof List)
		{
			return toJSON((List)data);
		}
		else if(data instanceof Map)
		{
			return toJSON((Map) data);
		}
		else if(data instanceof Set)
		{
			return toJSON((Set) data);
		}
		else 
		{
			return toJSON(data.toString());
		}
	}
	
	public static void main(String[] args)
	{
		HashMap map=new HashMap();
		final HashMap mapdata=new HashMap();
		map.put("testboolean", new Boolean(true));
		map.put("testint", 1);
		mapdata.put("teststr", "test string value");
		map.put("datamap", mapdata);
		mapdata.put("testmap", JsonToolKit.getToolKit().toJSON(mapdata));
		
		System.out.println(JsonToolKit.getToolKit().toJSON(map));
	}

	
}
