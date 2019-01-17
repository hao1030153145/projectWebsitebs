package com.lianNLP.website.integration;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.integration.DataService;
import com.lianNLP.website.web.po.RequirePO;

import java.util.List;
import java.util.Map;

/**
 * 钱包访问接口
 *
 * @author haolen
 * @version 1.0
 * @see
 */
public interface GatherExpDataService extends DataService {

    /**
     * 保存需求
     *
     * @param requirePO
     * @return
     * @throws BizException
     */
    Integer saveRequire(RequirePO requirePO) throws BizException;


    /**
     * 获得需求
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<RequirePO> getRequire(Map<String, Object> param) throws BizException;

    /**
     * 获得需求数量
     *
     * @return
     * @throws BizException
     */
    Integer getRequireCount() throws BizException;

}