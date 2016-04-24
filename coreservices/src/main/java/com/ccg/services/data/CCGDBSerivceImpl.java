package com.ccg.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccg.common.JsonToolKit;
import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.entity.CCGArticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("CCGDBService")
public class CCGDBSerivceImpl implements CCGDBService {

	@Autowired	
	private CCGArticleDAO articleDAO;
	@Override
	public void saveArticle(CCGArticle article) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getArticleCouont() {
		// TODO Auto-generated method stub
		return articleDAO.countAll();
	}
	@Override
	public String getArticleListJson() {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tmp=new ArrayList<Map<String,Object>>();
		List<CCGArticle> res=articleDAO.findAll();		
		for(CCGArticle art:res)
		{
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("text","Doc"+art.getTitle());
			map.put("cls","file");
			map.put("leaf",new Boolean(true));
			tmp.add(map);
		}
		return JsonToolKit.getToolKit().toJSON(tmp);
	}

}
