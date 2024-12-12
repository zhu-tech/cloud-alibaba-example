package com.neyogoo.example.admin.test5.service;

import com.baidu.fsg.uid.UidGenerator;
import com.neyogoo.example.admin.test5.api.Test6Api;
import com.neyogoo.example.admin.test5.dao.TUserMapper;
import com.neyogoo.example.admin.test5.model.TUser;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.SilenceException;
import com.neyogoo.example.common.core.util.RUtils;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Test5Service {

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private Test6Api test6Api;

    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean testCommit() {
        System.out.println("请求 test6 commit");
        System.out.println("XID = " + RootContext.getXID());
        return true;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean testRollback() {
        System.out.println("请求 test6 rollback");
        System.out.println("XID = " + RootContext.getXID());
        throw SilenceException.wrap("rollback exception");
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean addUser() {
        System.out.println("请求 test5 add user");
        System.out.println("XID = " + RootContext.getXID());
        TUser user = new TUser();
        user.setId(uidGenerator.getUid());
        user.setName("from test5");
        userMapper.insert(user);

        ContextUtils.setXTransactionId(RootContext.getXID());
        RUtils.getDataThrowEx(test6Api.addUser());
        return true;
    }
}
