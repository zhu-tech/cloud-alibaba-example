package com.neyogoo.example.common.database.properties;

/**
 * Id 生成方式
 */
public enum IdType {

    /**
     * 单机部署 或者 有固定部署集群数量时，使用 hutool 的
     */
    Hutool,
    /**
     * 集群部署动态扩容时使用
     * UidGenerator通过借用未来时间来解决 sequence 天然存在的并发限制
     */
    Default,
    /**
     * 集群部署动态扩容时使用
     * 采用 RingBuffer 来缓存已生成的 UID, 并行化 UID 的生产和消费, 同时对 CacheLine 补齐，避免了由 RingBuffer 带来的硬件级「伪共享」问题
     */
    Cache;

    public boolean eq(IdType t) {
        return t != null && this.name().equals(t.name());
    }
}
