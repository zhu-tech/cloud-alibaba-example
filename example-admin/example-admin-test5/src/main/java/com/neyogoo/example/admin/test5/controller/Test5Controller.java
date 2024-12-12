package com.neyogoo.example.admin.test5.controller;

import com.neyogoo.example.admin.test5.service.Test5Service;
import com.neyogoo.example.common.core.model.R;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test5")
public class Test5Controller {

    @Autowired
    private Test5Service test5Service;

    @GlobalTransactional(rollbackFor = Exception.class)
    @GetMapping("/anno/test/commit")
    public R<Boolean> testCommit() {
        return R.success(test5Service.testCommit());
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @GetMapping("/anno/test/rollback")
    public R<Boolean> testRollback() {
        return R.success(test5Service.testRollback());
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @GetMapping("/anno/add/user")
    public R<Boolean> addUser() {
        return R.success(test5Service.addUser());
    }
}
