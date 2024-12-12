package com.neyogoo.example.biz.toolbox.vo.response.msg;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class MailMsgRespVO {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("发送机构id")
    private Long sendOrgId;

    @ApiModelProperty("发送人Id")
    private Long sendUserId;

    @ApiModelProperty("发送人姓名")
    private String sendUserName;

    @ApiModelProperty("接收机构id")
    private Long receiveOrgId;

    @ApiModelProperty("接收人id")
    private Long receiveUserId;

    @ApiModelProperty("接收人姓名")
    private String receiveUserName;

    @ApiModelProperty("消息标题")
    private String msgTitle;

    @ApiModelProperty("消息文本")
    private String msgContent;

    @ApiModelProperty("发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty("已读时间")
    private LocalDateTime readTime;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("扩展内容")
    private Map<String, Object> ext;

    public static MailMsgRespVO fromModel(MailMsg model) {
        return BeanUtil.toBean(model, MailMsgRespVO.class);
    }

    public static List<MailMsgRespVO> fromModels(List<MailMsg> models) {
        return EntityUtils.toBeanList(models, MailMsgRespVO.class);
    }
}
