package com.neyogoo.example.biz.toolbox.vo.request.msg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "EmailReqDTO", description = "发送邮件请求入参")
public class EmailReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("收件人")
    @NotEmpty(message = "收件人不能为空")
    private List<String> receive;

    @ApiModelProperty("邮件主题")
    @NotEmpty(message = "邮件主题不能为空")
    private String subject;

    @ApiModelProperty("邮件内容")
    @NotEmpty(message = "邮件内容不能为空")
    private String text;

    @ApiModelProperty("抄送人")
    private List<String> cc;
}

