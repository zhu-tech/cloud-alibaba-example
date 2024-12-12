package com.neyogoo.example.common.database.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.util.AntiSqlFilterUtils;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class SqlPageUtils {

    /**
     * 构建分页对象
     */
    public <T> IPage<T> buildPage(PageReq params) {
        if (StrUtil.isEmpty(params.getSort())) {
            return new Page(params.getCurrent(), params.getSize());
        }
        Page<T> page = new Page(params.getCurrent(), params.getSize());
        List<OrderItem> orders = new ArrayList();
        List<String> sortArr = StrUtil.split(params.getSort(), ",");
        List<String> orderArr = StrUtil.split(params.getOrder(), ",");
        int len = Math.min(sortArr.size(), orderArr.size());

        for (int i = 0; i < len; ++i) {
            String humpSort = sortArr.get(i);
            String underlineSort = StrUtil.toUnderlineCase(humpSort);
            if (!StrUtil.equalsAny(humpSort, new CharSequence[]{"createTime", "updateTime"})) {
                underlineSort = AntiSqlFilterUtils.getSafeValue(underlineSort);
            }

            orders.add(StrUtil.equalsAny(orderArr.get(i), new CharSequence[]{"ascending", "ascend", "asc"})
                    ? OrderItem.asc(underlineSort) : OrderItem.desc(underlineSort));
        }
        page.setOrders(orders);
        return page;
    }

    /**
     * 构建分页对象，支持多个字段排序，用法：
     * eg.1, 参数：{order:"name,id", order:"desc,asc" }。 排序： name desc, id asc
     * eg.2, 参数：{order:"name", order:"desc,asc" }。 排序： name desc
     * eg.3, 参数：{order:"name,id", order:"desc" }。 排序： name desc
     *
     * @param entityClazz 字段中标注了@TableName 或 @TableId 注解的实体类。
     * @return 分页对象
     */
    public static <T> IPage<T> buildPage(PageReq params, Class<?> entityClazz) {
        // 没有排序参数
        if (StrUtil.isEmpty(params.getSort())) {
            return new Page(params.getCurrent(), params.getSize());
        }

        Page<T> page = new Page(params.getCurrent(), params.getSize());

        List<OrderItem> orders = new ArrayList<>();
        String[] sortArr = StrUtil.splitToArray(params.getSort(), StrPool.COMMA);
        String[] orderArr = StrUtil.splitToArray(params.getOrder(), StrPool.COMMA);

        int len = Math.min(sortArr.length, orderArr.length);
        for (int i = 0; i < len; i++) {
            String humpSort = sortArr[i];
            // 简单的 驼峰 转 下划线
            String underlineSort = Wraps.getDbField(humpSort, entityClazz);
            orders.add(StrUtil.equalsAny(orderArr[i], "ascending", "ascend", "asc")
                    ? OrderItem.asc(underlineSort) : OrderItem.desc(underlineSort));
        }
        page.setOrders(orders);
        return page;
    }

    /**
     * 将 Mybatis 的 IPage 对象转换为系统分页响应对象 PageResp
     */
    public static <T> PageResp<T> transferResp(IPage<T> page) {
        return PageResp.<T>builder()
                .records(page.getRecords())
                .total(page.getTotal())
                .current(page.getCurrent())
                .size(page.getSize())
                .build();
    }

    /**
     * 将 Mybatis 的 IPage 对象转换为系统分页响应对象 PageResp
     */
    public static <T, R> PageResp<R> transferResp(IPage<T> page, Function<T, R> transferFunc) {
        return PageResp.<R>builder()
                .records(page.getRecords().stream().map(transferFunc).collect(Collectors.toList()))
                .total(page.getTotal())
                .current(page.getCurrent())
                .size(page.getSize())
                .build();
    }


    /**
     * 转化Page 对象
     *
     * @param page             分页对象
     * @param destinationClass 目标类型
     * @return 目录分页对象
     */
    public static <T, E> IPage<T> toBeanPage(IPage<E> page, Class<T> destinationClass) {
        if (page == null || destinationClass == null) {
            return null;
        }
        IPage<T> newPage = new Page<>(page.getCurrent(), page.getSize());
        newPage.setPages(page.getPages());
        newPage.setTotal(page.getTotal());

        List<E> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return newPage;
        }

        List<T> destinationList = EntityUtils.toBeanList(list, destinationClass);
        newPage.setRecords(destinationList);
        return newPage;
    }
}
