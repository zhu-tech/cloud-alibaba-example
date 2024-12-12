package com.neyogoo.example.biz.toolbox.controller;

import com.neyogoo.example.biz.toolbox.service.ext.EnumService;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CheckSignature
@RestController
@RequestMapping("/v1/enum")
@Api(value = "Enum", tags = "枚举")
public class EnumController {

    @Autowired
    private EnumService enumService;

    @ApiOperation("获取当前系统枚举所有名称")
    @GetMapping("/names")
    public R<List<String>> enumNames() {
        return R.success(enumService.enumNames());
    }

    @ApiOperation("获取当前系统指定枚举")
    @PostMapping("/values")
    public R<Map<String, Map<String, String>>> enumsValue(@RequestBody List<String> enumNames) {
        return R.success(enumService.enumsValue(enumNames));
    }
}
