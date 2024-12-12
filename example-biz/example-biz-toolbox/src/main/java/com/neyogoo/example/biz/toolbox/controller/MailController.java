package com.neyogoo.example.biz.toolbox.controller;

import com.neyogoo.example.biz.toolbox.service.msg.MailMsgService;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgRemindReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgSaveReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.msg.MailMsgRespVO;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/mail")
@Api(value = "Mail", tags = "站内信")
public class MailController {

    @Autowired
    private MailMsgService mailMsgService;

    @ApiOperation("根据id查询站内信信息")
    @GetMapping("/get/byId/{id}")
    public R<MailMsgRespVO> getById(@PathVariable("id") @NotNull String id) {
        return R.success(MailMsgRespVO.fromModel(mailMsgService.getById(id)));
    }

    @ApiOperation("查询站内信列表")
    @PostMapping("/page")
    public R<PageResp<MailMsgRespVO>> page(@RequestBody @Validated PageReq<MailMsgPageReqVO> pageReqVo) {
        pageReqVo.getModel().setReceiveUserId(ContextUtils.getUserId());
        return R.success(mailMsgService.queryPage(pageReqVo));
    }

    @RepeatSubmit
    @ApiOperation("批量新增站内信")
    @PostMapping("/save/batch")
    public R<Boolean> saveBatch(@RequestBody @Validated MailMsgSaveReqVO saveReqVO) {
        return R.success(mailMsgService.saveBatch(saveReqVO));
    }

    @ApiOperation("查询站内信消息提醒")
    @PostMapping("/reminder")
    public R<List<MailMsgRespVO>> queryMsgReminder(@RequestBody @Validated MailMsgRemindReqVO bizMsgReminder) {
        bizMsgReminder.setReceiveUserId(ContextUtils.getUserId());
        return R.success(MailMsgRespVO.fromModels(mailMsgService.queryMsgReminder(bizMsgReminder)));
    }

    @RepeatSubmit
    @ApiOperation("根据id修改为已读")
    @PostMapping("/readOne/{id}")
    public R<Boolean> updateReadOneById(@PathVariable("id") @NotNull String id) {
        return R.success(mailMsgService.updateReadOneById(id));
    }

    @RepeatSubmit
    @ApiOperation("根据id列表批量修改为已读")
    @PostMapping("/readAll")
    public R<Boolean> updateReadAllByIds(@RequestParam("ids") @NotEmpty List<String> ids) {
        return R.success(mailMsgService.updateReadAllByIds(ids));
    }

    @RepeatSubmit
    @ApiOperation("根据id列表删除站内信")
    @DeleteMapping("/delete")
    public R<Boolean> deleteById(@RequestParam("ids") @NotEmpty List<String> ids) {
        return R.success(mailMsgService.deleteByIds(ids));
    }
}
