package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.OrgService;
import com.neyogoo.example.admin.sys.service.ext.OrgQuerySerivice;
import com.neyogoo.example.admin.sys.vo.response.org.OrgDeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgSimpleRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgTreeRespVO;
import com.neyogoo.example.biz.common.enumeration.sys.OrgCategoryEnum;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/org/query")
@Api(value = "OrgQuery", tags = "机构 - 查询")
public class OrgQueryController {

    @Autowired
    private OrgService orgService;
    @Autowired
    private OrgQuerySerivice orgQuerySerivice;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<OrgGetRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(orgQuerySerivice.getById(id));
    }

    @ApiOperation("根据id查询名字")
    @GetMapping("/get/name/byId/{id}")
    public R<String> getNameById(@PathVariable("id") @NotNull Long id) {
        return R.success(orgService.getNameById(id));
    }

    @ApiOperation("根据id列表查询名字")
    @PostMapping("/map/names/byIds")
    public R<Map<Long, String>> mapNamesByIds(@RequestBody Collection<Long> ids) {
        return R.success(orgService.mapNamesByIds(ids));
    }

    @ApiOperation("机构树")
    @GetMapping("/tree")
    public R<List<OrgTreeRespVO>> findTree() {
        return R.success(orgQuerySerivice.findTree());
    }

    @ApiOperation("机构简易列表")
    @GetMapping("/list/simple")
    public R<List<OrgSimpleRespVO>> listSimple(
            @RequestParam(required = false) OrgCategoryEnum orgCategory,
            @RequestParam(required = false) Integer orgLevel) {
        return R.success(orgQuerySerivice.listSimple(orgCategory, orgLevel));
    }

    @ApiOperation("查询机构（包含子机构）部门列表")
    @GetMapping("/list/dept")
    public R<List<OrgDeptRespVO>> listOrgDept(@RequestParam(required = false) String orgName) {
        return R.success(orgQuerySerivice.listOrgDept(orgName));
    }

    @ApiOperation("当前登录机构的直属子机构id")
    @GetMapping("/list/childrenIds")
    public R<List<Long>> listChildrenIds() {
        return R.success(orgService.listChildrenIds(ContextUtils.getOrgId()));
    }

    @ApiOperation("根据类型查询")
    @GetMapping("/list/byOrgCategory/{orgCategory}")
    public R<List<OrgSimpleRespVO>> listByOrgCategory(
            @PathVariable("orgCategory") @NotNull OrgCategoryEnum orgCategory) {
        return R.success(orgQuerySerivice.listByOrgCategory(orgCategory));
    }

    @ApiOperation("自动生成机构编号")
    @GetMapping("/generate/orgCode")
    public R<String> generateOrgCode() {
        return R.success(RandomStringUtils.randomNumeric(7));
    }
}
