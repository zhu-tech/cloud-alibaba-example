package com.neyogoo.example.admin.sys.dao;

import com.neyogoo.example.admin.sys.model.UserLogin;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户登录相关记录汇总
 */
@Repository
public interface UserLoginMapper extends SuperMapper<UserLogin> {

}
