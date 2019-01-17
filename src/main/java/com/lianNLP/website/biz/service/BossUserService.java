package com.lianNLP.website.biz.service;

import com.jeeframework.logicframework.biz.service.BizService;
import com.lianNLP.website.integration.bo.BossUser;
import com.lianNLP.website.web.filter.GetBossUsersFilter;
import com.lianNLP.website.web.filter.UpdateBossUserFilter;

import java.util.List;

/**
 * @author lanceyan
 */
public interface BossUserService extends BizService {

    BossUser getBossUserByUid(long uid);

    int addBossUser(BossUser bossUser);

    int updateBossUser(UpdateBossUserFilter updateBossUserFilter);


    int deleteBossUser(long uid);

    /**
     * 简单描述：根据用户名、密码返回用户对象
     * <p>
     *
     * @param userName
     * @param password
     * @
     */
    BossUser getBossUserByPasswd(String userName, String password);

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p>
     *
     * @param getBossUsersFilter
     * @
     */
    List<BossUser> getBossUsers(GetBossUsersFilter getBossUsersFilter);


}