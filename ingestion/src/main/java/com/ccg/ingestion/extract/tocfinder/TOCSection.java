package com.ccg.ingestion.extract.tocfinder;

public class TOCSection {
	String data;  // capital only	
	String[] _s;
	
	public String toString()
	{
		StringBuffer buf=new StringBuffer();
		if(_s!=null)
		{
			for(String ele:_s)
			{
				buf.append("["+ele+"]-");
			}
		}
		return new String(buf);
	}
	public TOCSection(String d)
	{
		data=d.toUpperCase().trim();
		_s=data.split("\\.");
	}
	
	public boolean isDirectChildSection(TOCSection sub)
	{
		if(sub._s.length==_s.length+1)
		{
			boolean matched=true;
			for(int i=0;i<_s.length;i++)
			{
				matched=sub._s[i].equals(_s[i])&&matched;
			}
			return matched;
		}
		return false;
	}
	public boolean isDirectParentSection(TOCSection parent)
	{
		return parent.isDirectChildSection(this);
	}
	
	public boolean isPrevSection(TOCSection prev)
	{
		int len=_s.length;
		if(_s.length==prev._s.length)
		{
			for(int i=0;i<len-1;i++)
			{
				if(!(_s[i].equals(prev._s[i])))
				{
					return false;
				}
			}
			// now if both are number
			// both are character
			//
			if(prev._s[len-1].length()<=_s[len-1].length())
			{
				try{
					int i_p=Integer.parseInt(prev._s[len-1]);
					int i_me=Integer.parseInt(_s[len-1]);
					return (i_p==i_me-1);	
				}
				catch(Exception e)
				{
					// one of them not integer
					char c_p=prev._s[len-1].charAt(0);
					char c_me=_s[len-1].charAt(0);
					return (c_p==c_me-1);
				}				
			}
		}
		return false;
	}
	
	public boolean isNextSection(TOCSection next)
	{
		return next.isPrevSection(this);
	}
}
