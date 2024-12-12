package com.neyogoo.example.common.mongodb.util;

import cn.hutool.core.util.StrUtil;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * spring data 分页插件工具类
 */
public class SpringDataPageUtils {

    /**
     * 构建 spring data 分页对象
     *
     * @param pageReq 系统分页对象
     */
    public static Pageable buildPageable(PageReq<?> pageReq) {
        String order = pageReq.getOrder();
        int page = translationSpringDataPage(pageReq);
        int size = Math.toIntExact(pageReq.getSize());
        if (StrUtil.isBlank(order)) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(
                page,
                size,
                Sort.Direction.fromString(pageReq.getOrder()),
                pageReq.getSort()
        );
    }

    /**
     * 转换 spring data 的 page
     *
     * @param pageReq 原 pageReq 对象
     * @return 实际的spring data的 page
     */
    private static int translationSpringDataPage(PageReq<?> pageReq) {
        return Math.toIntExact(pageReq.getCurrent() <= 0 ? 0 : pageReq.getCurrent() - 1);
    }

    /**
     * spring data分页转换为系统分页
     *
     * @param page 分页对象
     * @param <T>  目标对象泛型
     */
    public static <T> PageResp<T> translationPageResp(PageReq<?> pageReq, Page<T> page) {
        return PageResp.<T>builder()
                .records(page.getContent())
                .size(pageReq.getSize())
                .current(pageReq.getCurrent())
                .total(page.getTotalElements())
                .build();
    }


    /**
     * spring data分页转换为系统分页
     *
     * @param page     分页对象
     * @param function 转换对象
     * @param <T>      原对象泛型
     * @param <F>      目标对象泛型
     */
    public static <T, F> PageResp<F> translationPageResp(PageReq<?> pageReq, Page<T> page, Function<T, F> function) {
        return PageResp.<F>builder()
                .records(
                        page.getContent()
                                .stream()
                                .map(function)
                                .collect(Collectors.toList())
                )
                .size(pageReq.getSize())
                .current(pageReq.getCurrent())
                .total(page.getTotalElements())
                .build();
    }
}
