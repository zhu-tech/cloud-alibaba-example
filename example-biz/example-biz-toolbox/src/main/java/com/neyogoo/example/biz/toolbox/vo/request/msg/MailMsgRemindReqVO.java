package com.neyogoo.example.biz.toolbox.vo.request.msg;

import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 查询站内信消息提醒入参
 */
@Data
public class MailMsgRemindReqVO {

    @ApiModelProperty("拉取秒数")
    @NotNull(message = "拉取秒数不能为空")
    private Long seconds;

    @ApiModelProperty(value = "接收人id", hidden = true)
    private Long receiveUserId;

    public Query toQueryParam() {
        Criteria criteria = new Criteria();
        criteria.and(MailMsg.Fields.receiveUserId).is(receiveUserId);
        criteria.and(MailMsg.Fields.readTime).isNull();
        if (seconds != null) {
            criteria.and(MailMsg.Fields.sendTime).gte(LocalDateTime.now().minusSeconds(seconds));
        }
        Query query = new Query();
        query.addCriteria(criteria);
        return query;
    }
}
