package com.neyogoo.example.biz.toolbox.vo.request.logging;

import com.neyogoo.example.biz.common.model.DateTimeBetween;
import com.neyogoo.example.common.log.model.SysOptLogMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Data
public class SysOptLogPageReqVO {

    @ApiModelProperty("登录名")
    private String userName;

    @ApiModelProperty("地址")
    private String requestUrl;

    @ApiModelProperty("操作时间")
    private DateTimeBetween requestTime;

    public Query toQueryParam() {
        Criteria criteria = new Criteria();
        // 用户名
        if (StringUtils.isNotBlank(getUserName())) {
            criteria.and(SysOptLogMsg.Fields.userName).regex(".*?" + getUserName() + ".*");
        }
        // 地址
        if (StringUtils.isNotBlank(getRequestUrl())) {
            criteria.and(SysOptLogMsg.Fields.requestUrl).regex(".*?" + getRequestUrl() + ".*");
        }
        // 操作时间-起
        if (requestTime != null && requestTime.getStartTime() != null) {
            criteria.and(SysOptLogMsg.Fields.requestTime).gte(requestTime.getStartTime());
        }
        // 操作时间-止
        if (requestTime != null && requestTime.getEndTime() != null) {
            criteria.and(SysOptLogMsg.Fields.requestTime).lte(requestTime.getEndTime());
        }
        Query query = new Query();
        query.addCriteria(criteria);
        return query;
    }
}
