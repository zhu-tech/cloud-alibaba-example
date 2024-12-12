# 模块介绍

##### example-authority

- 权限校验模块。
- 提供 @CheckAuthority 注解功能。

##### example-boot

- springboot 集成模块。
- 提供 @CheckLoginToken 进行用户身份等校验。
- 提供 @CheckSignature 进行接口验签校验。
- 提供 @RepeatSubmit 进行接口重复提交校验。
- 提供 接口全局异常异常处理和包装。
- 提供 @Async 异步任务线程池配置。
- 提供 Header 参数和 ThreadLocal 存储转换。
- 提供 基础 Bean 配置和 Spring 上下文 Holder。

##### example-cloud

- springcloud 集成模块。
- 提供 OpenFeign 和 RestTemplate 拦截处理。

##### example-core

- 核心代码模块。
- 提供 常用 Model 定义。
- 提供 常用 Util 工具。
- 提供 Json 常用类型转换。
- 提供 通用异常定义。
- 提供 全局常量定义。

##### example-database

- mybatis-plus 集成模块。
- 提供 父级 Model、Service、Mapper 等定义。
- 提供 MyBatis 查询相关 Util。
- 提供 通用自定义类型转换。
- 提供 相关配置读取与加载。

##### example-dependencies

- 全部包版本管理。

##### example-log

- 日志管理模块。
- 包括操作日志（SysOptLog）和 Logback 日志。

##### example-mongodb

- MongoDB 集成模块。
- 提供 默认配置设置。
- 提供 常用类型转换。
- 提供 MongoDB 查询相关 Util。

##### example-mq

- RabbitMQ 集成模块。
- 提供 默认配置设置。
- 提供 消息异常处理。
- 提供 ThreadLocal 参数转换。

##### example-redis

- Redis 集成模块。
- 提供 默认配置设置。
- 提供 分布式锁工具。
- 提供 Redis 方法操作 Repository。

##### example-swagger

- Swagger 集成模块。
- 提供 默认配置设置。

##### example-token

- Token 管理集成模块。
- 提供 Token 对象定义。
- 提供 Token 相关 Util。
- 提供 Token 相关常量。

##### example-uid

- Uid 生成模块。
- 提供 Hutool 的 Id 生成策略。
- 提供 Baidu 的分布式 Id 生成策略。

##### example-xss

- Xss 防御模块。
- 提供 接口输入内容 Xss 检查。
