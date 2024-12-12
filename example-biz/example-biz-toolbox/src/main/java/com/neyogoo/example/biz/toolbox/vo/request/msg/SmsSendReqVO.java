package com.neyogoo.example.biz.toolbox.vo.request.msg;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.util.ListUtils;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.neyogoo.example.biz.common.enumeration.toolbox.SmsTemplateEnum;
import com.neyogoo.example.biz.toolbox.model.msg.SmsMsg;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 发送短信通知-入参
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "发送短信通知入参")
public class SmsSendReqVO {

    @ApiModelProperty("短信模板编码")
    @NotNull(message = "短信模板编码不能为空")
    private String templateCode;

    @ApiModelProperty("手机号")
    @NotEmpty(message = "手机号码不能为空")
    private List<String> phoneNumbers;

    @ApiModelProperty("模板所需参数")
    private Map<String, Object> params;

    @ApiModelProperty("发送人Id")
    private Long sendUserId;

    @ApiModelProperty("发送人姓名")
    private String sendUserName;

    @ApiModelProperty("短信标题")
    private String smsTitle;

    @ApiModelProperty(value = "发送时间", hidden = true)
    private LocalDateTime sendTime;

    public List<SmsMsg> toModels(SendSmsResponse response) {
        //获取信息
        String smsTitle = SmsTemplateEnum.getByTemplateCode(this.getTemplateCode()).getSmsTitle();
        LocalDateTime time = LocalDateTime.now();
        SmsMsg.SendSmsResponse smsResponse = BeanUtil.toBean(response, SmsMsg.SendSmsResponse.class);

        List<SmsMsg> models = ListUtils.newArrayListWithExpectedSize(phoneNumbers.size());
        this.getPhoneNumbers().forEach(o -> {
            SmsMsg sendMsg = BeanUtil.toBean(this, SmsMsg.class);
            sendMsg.setOrgId(ContextUtils.getOrgId())
                    .setPhoneNumber(o)
                    .setTemplateParams(params)
                    .setSendTime(time)
                    .setSmsTitle(smsTitle)
                    .setSendSmsResponse(smsResponse);
            models.add(sendMsg);
        });
        return models;
    }

    public SmsMsg toModel() {
        SmsMsg sendMsg = BeanUtil.toBean(this, SmsMsg.class);
        sendMsg.setOrgId(ContextUtils.getOrgId())
                .setPhoneNumber(this.getPhoneNumbers().get(0))
                .setTemplateParams(params)
                .setSendTime(LocalDateTime.now())
                .setSmsTitle(SmsTemplateEnum.getByTemplateCode(this.getTemplateCode()).getSmsTitle());
        return sendMsg;
    }
}
