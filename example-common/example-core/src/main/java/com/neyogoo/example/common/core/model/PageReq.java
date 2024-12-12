package com.neyogoo.example.common.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PageReq", description = "分页参数")
public class PageReq<T> {

    @NotNull(message = "查询对象model不能为空")
    @ApiModelProperty(value = "查询参数", required = true)
    @Valid
    private T model;

    @ApiModelProperty("页面大小")
    private long size = 10;

    @ApiModelProperty("当前页")
    private long current = 1;

    @ApiModelProperty("排序")
    private String sort;

    @ApiModelProperty(value = "排序规则", allowableValues = "desc,asc")
    private String order;

    @ApiModelProperty("扩展参数")
    private Map<String, Object> extra;

    /**
     * 计算当前分页偏移量
     */
    @JsonIgnore
    public long offset() {
        long current = this.current;
        if (current <= 1L) {
            return 0L;
        }
        return (current - 1) * this.size;
    }

    @JsonIgnore
    public PageReq<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.put(key, value);
        return this;
    }

    @JsonIgnore
    public PageReq<T> putAll(Map<String, Object> extra) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.putAll(extra);
        return this;
    }

    public <R> PageResp<R> toEmptyPageResp(Class<R> clazz) {
        PageResp<R> respPage = new PageResp<>(this.current, this.size, 0);
        respPage.setRecords(new ArrayList<>());
        return respPage;
    }
}
