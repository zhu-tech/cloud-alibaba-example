package com.neyogoo.example.common.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neyogoo.example.common.core.model.Pair;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


@UtilityClass
public class EntityUtils {

    /**
     * list转成list
     */
    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> transformFunc) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream().map(transformFunc).collect(Collectors.toList());
    }

    /**
     * 不重复list
     */
    public static <T, R> List<R> distinctList(Collection<T> collection, Function<T, R> func) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream().map(func).distinct().collect(Collectors.toList());
    }

    /**
     * 复制list
     */
    public static <T, R> List<R> copyList(Collection<T> collection, Class<R> clazz) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        return toList(collection, o -> BeanUtil.toBean(o, clazz));
    }

    /**
     * map转pairList
     */
    public static <K, V> List<Pair<K, V>> toPairList(Map<K, V> map) {
        if (MapUtil.isEmpty(map)) {
            return new ArrayList<>();
        }
        return map.entrySet().stream().map(Pair::new).collect(Collectors.toList());
    }

    /**
     * list转成set
     */
    public static <T, R> Set<R> toSet(Collection<T> collection, Function<T, R> func) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashSet<>();
        }
        return collection.stream().map(func).collect(Collectors.toSet());
    }

    /**
     * list转成map
     */
    public static <K, V> Map<K, V> toMap(Collection<V> collection, Function<V, K> getKeyFunc) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(16);
        }
        return collection.stream().collect(Collectors.toMap(getKeyFunc, Function.identity()));
    }

    /**
     * list转成map
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> getKeyFunc,
                                            Function<T, V> getValueFunc) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(16);
        }
        return collection.stream().collect(Collectors.toMap(getKeyFunc, getValueFunc));
    }

    /**
     * list转成linkedHashMap
     */
    public static <T, K, V> Map<K, V> toLinkedHashMap(Collection<T> collection, Function<T, K> getKeyFunc,
                                                      Function<T, V> getValueFunc) {
        if (CollectionUtils.isEmpty(collection)) {
            return new LinkedHashMap<>(16);
        }
        return collection.stream().collect(Collectors.toMap(
                getKeyFunc, getValueFunc,
                (oldValue, newValue) -> newValue,
                LinkedHashMap::new
        ));
    }

    /**
     * list转成分组map
     */
    public static <K, V> Map<K, List<V>> groupingBy(Collection<V> collection, Function<V, K> getKeyFunc) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(16);
        }
        return collection.stream().collect(Collectors.groupingBy(getKeyFunc));
    }

    /**
     * list转成分组map
     */
    public static <T, K, R> Map<K, List<R>> groupingBy(Collection<T> collection, Function<T, K> getKeyFunc,
                                                       Function<T, R> toValueFunc) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(16);
        }
        return collection.stream().collect(Collectors.groupingBy(
                getKeyFunc,
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        l -> l.stream().map(toValueFunc).collect(Collectors.toList())
                )
        ));
    }

    /**
     * 切分成小list批量执行
     */
    public static <T> void batchConsume(final List<T> list, int subSize, Consumer<List<T>> consumer) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        if (subSize < 1) {
            subSize = 1;
        }
        int total = list.size();
        int index = subSize;
        for (int i = 0; i < list.size(); i += subSize) {
            if (i + subSize > total) {
                index = total - i;
            }
            consumer.accept(list.subList(i, i + index));
        }
    }

    /**
     * 转换 list
     *
     * @param sourceList       源集合
     * @param destinationClass 目标类型
     * @return 目标集合
     */
    public static <T, E> List<T> toBeanList(Collection<E> sourceList, Class<T> destinationClass) {
        if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
            return Collections.emptyList();
        }
        return sourceList.parallelStream()
                .filter(Objects::nonNull)
                .map(source -> BeanUtil.toBean(source, destinationClass))
                .collect(Collectors.toList());
    }

    /**
     * 多个map合并成一个
     */
    public static <K, V> Map<K, V> putAll(Map<K, V>... maps) {
        int size = 0;
        for (Map<K, V> map : maps) {
            size += map.size();
        }
        Map<K, V> result;
        if (maps[0] instanceof LinkedHashMap) {
            result = Maps.newLinkedHashMapWithExpectedSize(size);
        } else if (maps[0] instanceof TreeMap) {
            result = new TreeMap<>();
        } else {
            result = Maps.newHashMapWithExpectedSize(size);
        }
        for (Map<K, V> map : maps) {
            result.putAll(map);
        }
        return result;
    }

    /**
     * 多个list合并成一个
     */
    public static <T> List<T> addAll(List<T>... lists) {
        int size = 0;
        for (List<T> list : lists) {
            size += list.size();
        }
        List<T> result;
        if (lists[0] instanceof ArrayList) {
            result = Lists.newArrayListWithCapacity(size);
        } else if (lists[0] instanceof LinkedList) {
            result = Lists.newLinkedList();
        } else {
            result = Lists.newArrayListWithCapacity(size);
        }
        for (List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    @SneakyThrows
    public static <T> void setIfNull(T source, T target, Class<T> clazz) {
        if (source == null || target == null) {
            return;
        }
        List<Field> fields = fieldsWithSuper(clazz).stream()
                .filter(o -> !Modifier.isFinal(o.getModifiers()))
                .filter(o -> !Modifier.isStatic(o.getModifiers()))
                .collect(Collectors.toList());
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(target) == null) {
                field.set(target, field.get(source));
            }
        }
    }

    private static List<Field> fieldsWithSuper(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Class temp = clazz;
        while (temp != null) {
            fields.addAll(Arrays.asList(temp.getDeclaredFields()));
            temp = temp.getSuperclass();
        }
        return fields;
    }

    /**
     * 取出map的key值交集，对每一个key执行func
     */
    public static <K, V> void consumerWithIntersectionKey(Map<K, V> map1, Map<K, V> map2, Consumer<K> func) {
        if (!ObjectUtils.allNotNull(map1, map2, func)) {
            return;
        }
        Set<K> set = Sets.intersection(map1.keySet(), map2.keySet());
        if (set.size() == 0) {
            return;
        }
        for (K key : set) {
            func.accept(key);
        }
    }

    /**
     * 取出map的key值差集（map2 - map1），对每一个key执行func
     */
    public static <K, V> void consumerWithDifferenceKey(Map<K, V> map1, Map<K, V> map2, Consumer<K> func) {
        if (!ObjectUtils.allNotNull(map1, map2, func)) {
            return;
        }
        Set<K> set = Sets.difference(map2.keySet(), map1.keySet());
        if (set.size() == 0) {
            return;
        }
        for (K key : set) {
            func.accept(key);
        }
    }

    /**
     * 转换成key为string类型的map
     */
    public static <V> Map<String, V> stringKeyMap(Map<Long, V> map) {
        if (map == null || map.size() == 0) {
            return Collections.emptyMap();
        }
        return map.entrySet().stream().collect(
                Collectors.toMap(o -> String.valueOf(o.getKey()), o -> o.getValue())
        );
    }

    /**
     * 计算list总和
     */
    public static <T> BigDecimal sumDecimal(List<T> list, Function<T, BigDecimal> getNumFunc) {
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        return list.stream().map(getNumFunc).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * 计算list总和
     */
    public static <T> Integer sumInt(List<T> list, Function<T, Integer> getNumFunc) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return list.stream().map(getNumFunc).reduce(Integer::sum).orElse(0);
    }

    @SneakyThrows
    public static <T> void trim(List<T> list, Class<T> clazz) {
        if (CollectionUtil.isEmpty(list) || clazz == null) {
            return;
        }
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(o -> !Modifier.isFinal(o.getModifiers()) && !Modifier.isStatic(o.getModifiers()))
                .filter(o -> o.getGenericType().getTypeName().contains(StrPool.STRING_TYPE_NAME))
                .map(o -> {
                    o.setAccessible(true);
                    return o;
                }).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(fields)) {
            return;
        }
        for (T entity : list) {
            for (Field field : fields) {
                String s = (String) field.get(entity);
                if (StringUtils.isNotBlank(s)) {
                    field.set(entity, s.trim());
                }
            }
        }
    }
}
