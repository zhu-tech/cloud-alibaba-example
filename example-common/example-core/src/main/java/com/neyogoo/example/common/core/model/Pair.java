package com.neyogoo.example.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pair<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private K key;
    private V value;

    public Pair(Map.Entry<K, V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    public static <K, V> Pair of(K k, V v) {
        return new Pair<>(k, v);
    }

    /**
     * pair list 转为 map
     */
    public static <K, V> Map<K, V> toMap(List<Pair<K, V>> pairs) {
        if (CollectionUtils.isEmpty(pairs)) {
            return new LinkedHashMap<>();
        }
        Map<K, V> map = new LinkedHashMap<>();
        pairs.forEach(o -> {
            if (map.containsKey(o.getKey())) {
                throw new RuntimeException("重复的key：" + o.getKey());
            }
            map.put(o.getKey(), o.getValue());
        });
        return map;
    }

    /**
     * map 转为 pair list
     */
    public static <K, V> List<Pair<K, V>> fromMap(Map<K, V> map) {
        if (CollectionUtils.isEmpty(map)) {
            return new ArrayList<>();
        }
        return map.entrySet().stream().map(o -> new Pair<>(o.getKey(), o.getValue())).collect(Collectors.toList());
    }
}
