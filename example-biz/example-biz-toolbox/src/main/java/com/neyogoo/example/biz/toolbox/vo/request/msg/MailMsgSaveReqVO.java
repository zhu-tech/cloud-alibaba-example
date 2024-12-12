package com.neyogoo.example.biz.toolbox.vo.request.msg;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(description = "批量保存站内信入参")
public class MailMsgSaveReqVO {

    @ApiModelProperty("发送人Id")
    private Long sendUserId;

    @ApiModelProperty("发送人姓名")
    private String sendUserName;

    @ApiModelProperty("接收人")
    @NotEmpty(message = "接收人不能为空")
    private Collection<ReceiveUser> receiveUsers;

    @ApiModelProperty("消息标题")
    @NotBlank(message = "消息标题不能为空")
    private String msgTitle;

    @ApiModelProperty("发送时间")
    @NotNull(message = "发送时间不能为空")
    private LocalDateTime sendTime;

    @ApiModelProperty("扩展内容")
    private Map<String, Object> ext;

    @Data
    public static class ReceiveUser {

        @ApiModelProperty("接收人机构id")
        private Long receiveOrgId;

        @ApiModelProperty("接收人id")
        @NotNull(message = "接收人id不能为空")
        private Long receiveUserId;

        @ApiModelProperty("接收人姓名")
        private String receiveUserName;

        @ApiModelProperty("消息文本")
        @NotNull(message = "消息文本不能为空")
        private String msgContent;

    }

    public List<MailMsg> toModels() {
        Long userId = ContextUtils.getUserId();
        LocalDateTime time = LocalDateTime.now();
        List<MailMsg> models = EntityUtils.toBeanList(this.getReceiveUsers(), MailMsg.class);
        models.forEach(model -> {
            BeanUtil.copyProperties(this, model);
            model.setSendOrgId(ContextUtils.getOrgId())
                    .setCreateUserId(userId).setCreateTime(time);
        });
        return models;
    }
}
