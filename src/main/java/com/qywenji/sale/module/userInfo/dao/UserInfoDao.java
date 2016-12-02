package com.qywenji.sale.module.userInfo.dao;

import com.qywenji.sale.commons.dao.BaseDao;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by CAI_GC on 2016/11/30.
 */
@Repository
public class UserInfoDao extends BaseDao<UserInfo> {


    public UserInfo getByOpenId(String openId) {
        String hql = "from UserInfo where openId=?";
        return super.get(hql,openId);
    }
}
