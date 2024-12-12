package com.neyogoo.example.common.core.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.core.util.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageResp<T> {

    @Builder.Default
    private List<T> records = Collections.emptyList();
    @Builder.Default
    private long total = 0;
    @Builder.Default
    private long size = 10;
    @Builder.Default
    private long current = 1;

    public PageResp(long current, long size) {
        this.current = current;
        this.size = size;
        this.total = 0;
        this.records = Collections.emptyList();
    }

    public PageResp(long current, long size, long total) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = Collections.emptyList();
    }

    public void cut(List<T> list) {
        this.records = ListUtil.page((int) this.current - 1, (int) this.size, list);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return CollUtil.isEmpty(this.records);
    }

    public <R> PageResp<R> convert(Function<T, R> function) {
        PageResp<R> pageResp = new PageResp<>();
        pageResp.current = this.current;
        pageResp.size = this.size;
        pageResp.total = this.total;
        pageResp.records = EntityUtils.toList(this.records, function);
        return pageResp;
    }
}
