package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGArticleMetadataDAO;
import com.ccg.dataaccess.entity.CCGArticleMetadata;
@Repository("CCGArticleMetadataDAO")
public class CCGArticleMetadataDAOImpl extends CCGBaseDAOImpl<CCGArticleMetadata, Integer>
		implements CCGArticleMetadataDAO {

}
