package com.neyogoo.example.biz.toolbox.vo.request.msg;

import com.neyogoo.example.biz.common.model.DateTimeBetween;
import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotNull;

/**
 * 查询站内信入参
 */
@Data
public class MailMsgPageReqVO {

    @ApiModelProperty(value = "接收人id", hidden = true)
    private Long receiveUserId;

    @ApiModelProperty("是否已读")
    @NotNull(message = "是否已读不可为空")
    private Boolean readFlag;

    @ApiModelProperty("发送时间")
    private DateTimeBetween sendTime;

    public Query toQueryParam() {
        Criteria criteria = new Criteria();
        // 接收人Id
        criteria.and(MailMsg.Fields.receiveUserId).is(getReceiveUserId());
        // 未读/已读
        if (readFlag) {
            criteria.and(MailMsg.Fields.readTime).ne(null);
        } else {
            criteria.and(MailMsg.Fields.readTime).isNull();
        }
        // 操作时间-起
        if (sendTime != null && sendTime.getStartTime() != null) {
            criteria.and(MailMsg.Fields.sendTime).gte(sendTime.getStartTime());
        }
        // 操作时间-止
        if (sendTime != null && sendTime.getEndTime() != null) {
            criteria.and(MailMsg.Fields.sendTime).lte(sendTime.getEndTime());
        }
        Query query = new Query();
        query.addCriteria(criteria);
        return query;
    }
}
