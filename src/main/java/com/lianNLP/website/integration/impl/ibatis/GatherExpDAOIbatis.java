package com.lianNLP.website.integration.impl.ibatis;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.lianNLP.website.integration.GatherExpDataService;
import com.lianNLP.website.web.po.RequirePO;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author hoalen
 * @version 1.0
 */
@Scope("prototype")
@Repository("gatherExpDataService")
public class GatherExpDAOIbatis extends BaseDaoiBATIS implements GatherExpDataService {


    @Override
    public Integer saveRequire(RequirePO requirePO) throws BizException {
        try{
            return sqlSessionTemplate.insert("gatherExpMapper.saveRequire", requirePO);
        }catch (DataAccessException e){
            throw new DAOException(e);
        }
    }

    @Override
    public List<RequirePO> getRequire(Map<String, Object> param) throws BizException {
        try{
            return sqlSessionTemplate.selectList("gatherExpMapper.getRequire", param);
        }catch (DataAccessException e){
            throw new DAOException(e);
        }
    }

    @Override
    public Integer getRequireCount() throws BizException {
        try{
            return sqlSessionTemplate.selectOne("gatherExpMapper.getRequireCount");
        }catch (DataAccessException e){
            throw new DAOException(e);
        }
    }
}