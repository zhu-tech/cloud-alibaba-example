package com.neyogoo.example.admin.test6.controller;

import com.neyogoo.example.admin.test6.service.Test6Service;
import com.neyogoo.example.common.core.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test6")
public class Test6Controller {

    @Autowired
    private Test6Service test6Service;

    @GetMapping("/anno/add/user")
    public R<Boolean> addUser() {
        return R.success(test6Service.addUser());
    }
}
