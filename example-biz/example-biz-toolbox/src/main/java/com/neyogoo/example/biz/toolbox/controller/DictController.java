package com.neyogoo.example.biz.toolbox.controller;

import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.model.Dict;
import com.neyogoo.example.biz.toolbox.service.DictService;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictMapReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictSaveReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictUpdateReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.dict.DictMapRespVO;
import com.neyogoo.example.biz.toolbox.vo.response.dict.DictRespVO;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.database.util.SqlPageUtils;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Validated
@CheckSignature
@RestController
@RequestMapping("/v1/dict")
@Api(value = "Dict", tags = "字典")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<DictRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(DictRespVO.fromModel(dictService.getById(id)));
    }

    @ApiOperation("根据类型查询字典列表")
    @GetMapping("/list/byType/{dictType}")
    public R<List<DictRespVO>> listByType(@PathVariable("dictType") @NotNull DictTypeEnum dictType) {
        return R.success(DictRespVO.fromModels(dictService.listByType(dictType)));
    }

    @ApiOperation("根据类型查询字典映射")
    @GetMapping("/map/byType/{dictType}")
    public R<Map<String, String>> mapByType(@PathVariable("dictType") @NotNull DictTypeEnum dictType) {
        return R.success(EntityUtils.toMap(dictService.listByType(dictType), Dict::getDictCode, Dict::getDictName));
    }

    @ApiOperation("根据类型和是否可用查询字典列表")
    @PostMapping("/map/byTypes")
    public R<Map<String, List<DictMapRespVO>>> mapByTypes(@RequestBody @Validated DictMapReqVO reqVO) {
        return R.success(dictService.mapByTypes(reqVO));
    }

    @ApiOperation("根据类型查询字典可用列表")
    @PostMapping("/map/usable/byTypes")
    public R<Map<String, Map<String, String>>> mapUsableByTypes(@RequestBody List<String> dictTypes) {
        return R.success(dictService.mapUsableByTypes(dictTypes));
    }

    @ApiOperation("分页")
    @PostMapping("/page")
    public R<PageResp<DictRespVO>> page(@RequestBody @Validated PageReq<DictPageReqVO> pageReq) {
        return R.success(SqlPageUtils.transferResp(dictService.findPage(pageReq), DictRespVO::fromModel));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<DictRespVO> save(@RequestBody @Validated DictSaveReqVO saveReqVO) {
        return R.success(DictRespVO.fromModel(dictService.save(saveReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<DictRespVO> update(@RequestBody @Validated DictUpdateReqVO updateReqVO) {
        return R.success(DictRespVO.fromModel(dictService.update(updateReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("启用禁用")
    @PostMapping("/update/usable/byId/{id}/{usable}")
    public R<Boolean> updateUsableById(@PathVariable("id") @NotNull Long id,
                                       @PathVariable("usable") @NotNull Boolean usable) {
        return R.success(dictService.updateUsableById(id, usable));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(dictService.removeById(id));
    }
}
