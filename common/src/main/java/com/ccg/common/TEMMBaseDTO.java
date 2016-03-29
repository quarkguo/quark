package com.ccg.common;
import java.util.HashMap;
import java.util.Map;



public class TEMMBaseDTO implements JsonDTO{
	public TEMMBaseDTO()
	{
	}
	
	public String getJSON() {
		// TODO Auto-generated method stub
		return JsonToolKit.getToolKit().toJSON(getDataMap());
	}

	public String toString(){
		Map<String,Object> data=getDataMap();
		StringBuffer buf=new StringBuffer();
		for(String key:data.keySet())
		{
			buf.append("["+key+":]"+data.get(key));
		}
		return new String(buf);
	}
	
	public Map<String,Object> getDataMap()
	{
		return new HashMap<String,Object>();
	}


	public String cleanQuote(String source)
	{
		String res=source.replaceAll("\"", "'");
		return res;
	}
	
	private int _seed=1;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _seed;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TEMMBaseDTO other = (TEMMBaseDTO) obj;
		if (_seed != other._seed)
			return false;
		return true;
	}
}
