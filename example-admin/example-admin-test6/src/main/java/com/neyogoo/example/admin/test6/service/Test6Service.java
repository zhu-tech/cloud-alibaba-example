package com.neyogoo.example.admin.test6.service;

import com.baidu.fsg.uid.UidGenerator;
import com.neyogoo.example.admin.test6.dao.TUserMapper;
import com.neyogoo.example.admin.test6.model.TUser;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.BizException;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Test6Service {

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private TUserMapper userMapper;

    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean addUser() {
        if (StringUtils.isNotBlank(ContextUtils.getXTransactionId())) {
            RootContext.bind(ContextUtils.getXTransactionId());
        }
        System.out.println("请求 test6 add user");
        System.out.println("XID = " + RootContext.getXID());
        TUser user = new TUser();
        user.setId(uidGenerator.getUid());
        user.setName("from test6");
        userMapper.insert(user);

        // throw BizException.wrap("roll back exception");
        return true;
    }
}
