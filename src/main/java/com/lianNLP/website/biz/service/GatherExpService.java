package com.lianNLP.website.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.lianNLP.website.web.po.RequirePO;

import java.util.List;
import java.util.Map;

/**
 * @author haolen
 * @version 1.0
 */
public interface GatherExpService extends BizService {

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