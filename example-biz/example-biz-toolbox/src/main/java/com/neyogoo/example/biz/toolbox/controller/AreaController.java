package com.neyogoo.example.biz.toolbox.controller;

import com.neyogoo.example.biz.toolbox.service.AreaService;
import com.neyogoo.example.biz.toolbox.vo.response.area.AreaRespVO;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/v1/area")
@Api(value = "Area", tags = "地区")
public class AreaController {


    @Autowired
    private AreaService areaService;

    @ApiOperation(value = "根据上级id查询子级列表", notes = "查询全部一级地区时，parentId 传 0")
    @GetMapping("/list/byParentId/{parentId}")
    public R<List<AreaRespVO>> listByParentId(@PathVariable("parentId") @NotNull Long parentId) {
        return R.success(AreaRespVO.fromModels(areaService.listByParentId(parentId)));
    }

    @ApiOperation("根据编码查询全称")
    @GetMapping("/get/fullName/byCode/{code}")
    public R<String> getFullNameByCode(@PathVariable("code") @NotBlank String code) {
        return R.success(areaService.getFullNameByCode(code));
    }

    @ApiOperation("根据编码列表查询编码和名称对应关系")
    @PostMapping("/map/names/byCodes")
    public R<Map<String, String>> mapNamesByCodes(@RequestBody @NotEmpty Collection<String> codes) {
        return R.success(areaService.getAreaNamesByCodes(codes));
    }
}
