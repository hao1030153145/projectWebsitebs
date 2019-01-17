package com.lianNLP.website.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.lianNLP.website.biz.service.GatherExpService;
import com.lianNLP.website.integration.GatherExpDataService;
import com.lianNLP.website.web.po.RequirePO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author haolen
 * @version 1.0
 */
@Service("gatherExpService")
public class GatherExpServicePojo extends BaseService implements GatherExpService {

    @Resource
    private GatherExpDataService gatherExpDataService;

    @Override
    public Integer saveRequire(RequirePO requirePO) throws BizException {
        return gatherExpDataService.saveRequire(requirePO);
    }

    @Override
    public List<RequirePO> getRequire(Map<String, Object> param) throws BizException {
        return gatherExpDataService.getRequire(param);
    }

    @Override
    public Integer getRequireCount() throws BizException {
        return gatherExpDataService.getRequireCount();
    }
}