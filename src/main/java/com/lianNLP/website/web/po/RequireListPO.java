package com.lianNLP.website.web.po;

import java.util.List;

/**
 * Created by Administrator on 2018/10/24.
 */
public class RequireListPO {

    private List<RequirePO> require;
    private int  count;


    public List<RequirePO> getRequire() {
        return require;
    }

    public void setRequire(List<RequirePO> require) {
        this.require = require;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
