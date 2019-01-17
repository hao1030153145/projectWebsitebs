package com.lianNLP.website.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.lianNLP.website.biz.service.BossUserService;
import com.lianNLP.website.integration.BossUserDataService;
import com.lianNLP.website.integration.bo.BossUser;
import com.lianNLP.website.web.filter.GetBossUsersFilter;
import com.lianNLP.website.web.filter.UpdateBossUserFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanceyan
 */
@Service("bossUserService")
public class BossUserServicePojo extends BaseService implements BossUserService {
    @Resource
    private BossUserDataService bossUserDataService;

    @Override
    public int addBossUser(BossUser bossUser) {
        try {
            return bossUserDataService.addBossUser(bossUser);
        } catch (DAOException e) {
            throw new BizException(e);
        }
    }

    @Override
    public int deleteBossUser(long uid) {
        try {
            return bossUserDataService.deleteBossUser(uid);
        } catch (DAOException e) {
            throw new BizException(e);

        }
    }

    @Override
    public int updateBossUser(UpdateBossUserFilter updateBossUserFilter) {
        try {
            return bossUserDataService.updateBossUser(updateBossUserFilter);
        } catch (DAOException e) {
            throw new BizException(e);
        }
    }

    @Override
    public BossUser getBossUserByPasswd(String userName, String password) {
        try {
            return bossUserDataService.getBossUserByPasswd(userName, password);
        } catch (DAOException e) {
            throw new BizException(e);
        }
    }

    @Override
    public BossUser getBossUserByUid(long uid) {
        try {
            return bossUserDataService.getBossUserByUid(uid);
        } catch (DAOException e) {
            throw new BizException(e);
        }
    }

    @Override
    public List<BossUser> getBossUsers(GetBossUsersFilter getBossUsersFilter) {
        try {
            return bossUserDataService.getBossUsers(getBossUsersFilter);
        } catch (DAOException e) {
            throw new BizException(e);
        }
    }


}