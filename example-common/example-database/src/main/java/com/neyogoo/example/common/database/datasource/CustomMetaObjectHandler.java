package com.neyogoo.example.common.database.datasource;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.SpringUtils;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.database.model.SavedEntity;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * MyBatis Plus 元数据处理类
 * 用于自动 注入 id, createTime, updateTime, createUserId, updateUserId 等字段
 * <p>
 * 判断逻辑：
 * 1. insert 方法，自动填充 id, createTime, updateTime, createUserId, updateUserId 字段，字段为空则自动生成，不为空则使用传递进来的
 * 2. update 方法，自动填充 id, updateTime, updateUserId 字段，字段为空则自动生成，不为空则使用传递进来的
 * <p>
 * 注入值：
 * id：  IdUtil.getSnowflake(workerId, dataCenterId);
 * createTime：LocalDateTime.now()
 * updateTime：LocalDateTime.now()
 * createUserId：BaseContextHandler.getUserId()
 * updateUserId：BaseContextHandler.getUserId()
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {


    private UidGenerator uidGenerator;

    public CustomMetaObjectHandler() {
        super();
    }

    /**
     * 注意：不支持 复合主键 自动注入！！
     * <p>
     * 1、所有的继承了Entity、SuperEntity的实体，在insert时，
     * id： id为空时， 通过IdGenerate生成唯一ID。
     * createUserId, updateUserId: 自动赋予 当前线程上的登录人id。
     * createTime, updateTime: 自动赋予 服务器的当前时间。
     * <p>
     * 注意：实体中字段为空时才会赋值，若手动传值了，这里不会重新赋值
     * <p>
     * 2、未继承任何父类的实体，且主键标注了 @TableId(value = "xxx", type = IdType.INPUT) 也能自动赋值，主键的字段名称任意
     * <p>
     * 3、未继承任何父类的实体，但主键名为 id，也能自动赋值
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        fillCreated(metaObject);
        fillUpdated(metaObject);
        fillId(metaObject);
    }

    private void fillId(MetaObject metaObject) {
        if (uidGenerator == null) {
            // 这里使用SpringUtils的方式"异步"获取对象，防止启动时，报循环注入的错
            uidGenerator = SpringUtils.getBean(UidGenerator.class);
        }
        Long id = uidGenerator.getUid();

        //1. 继承了SuperEntity 若 ID 中有值，就不设置
        if (metaObject.getOriginalObject() instanceof SavedEntity) {
            Object oldId = ((SavedEntity) metaObject.getOriginalObject()).getId();
            if (oldId != null) {
                return;
            }
            Object idVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(SavedEntity.ID_FIELD).getName())
                    ? String.valueOf(id) : id;
            this.setFieldValByName(SavedEntity.ID_FIELD, idVal, metaObject);
            return;
        }

        // 2. 没有继承SuperEntity， 但主键的字段名为：  id
        if (metaObject.hasGetter(SavedEntity.ID_FIELD)) {
            Object oldId = metaObject.getValue(SavedEntity.ID_FIELD);
            if (oldId != null) {
                return;
            }

            Object idVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(SavedEntity.ID_FIELD).getName())
                    ? String.valueOf(id) : id;
            this.setFieldValByName(SavedEntity.ID_FIELD, idVal, metaObject);
            return;
        }

        // 3. 实体没有继承 Entity 和 SuperEntity，且 主键名为其他字段
        TableInfo tableInfo = TableInfoHelper.getTableInfo(metaObject.getOriginalObject().getClass());
        if (tableInfo == null) {
            return;
        }
        // 主键类型
        Class<?> keyType = tableInfo.getKeyType();
        if (keyType == null) {
            return;
        }
        // id 字段名
        String keyProperty = tableInfo.getKeyProperty();
        Object oldId = metaObject.getValue(keyProperty);
        if (oldId != null) {
            return;
        }

        // 反射得到 主键的值
        Field idField = ReflectUtil.getField(metaObject.getOriginalObject().getClass(), keyProperty);
        Object fieldValue = ReflectUtil.getFieldValue(metaObject.getOriginalObject(), idField);
        // 判断ID 是否有值，有值就不
        if (ObjectUtil.isNotEmpty(fieldValue)) {
            return;
        }
        Object idVal = keyType.getName().equalsIgnoreCase(StrPool.STRING_TYPE_NAME) ? String.valueOf(id) : id;
        this.setFieldValByName(keyProperty, idVal, metaObject);
    }

    private void fillCreated(MetaObject metaObject) {
        // 设置创建时间和创建人
        if (metaObject.getOriginalObject() instanceof SavedEntity) {
            created(metaObject);
            return;
        }

        if (metaObject.hasGetter(UpdatedEntity.CREATE_USER_ID)) {
            Object oldVal = metaObject.getValue(UpdatedEntity.CREATE_USER_ID);
            if (oldVal == null) {
                this.setFieldValByName(UpdatedEntity.CREATE_USER_ID, ContextUtils.getUserId(), metaObject);
            }
        }
        if (metaObject.hasGetter(UpdatedEntity.CREATE_TIME)) {
            Object oldVal = metaObject.getValue(UpdatedEntity.CREATE_TIME);
            if (oldVal == null) {
                this.setFieldValByName(UpdatedEntity.CREATE_TIME, LocalDateTime.now(), metaObject);
            }
        }

    }

    private void created(MetaObject metaObject) {
        SavedEntity entity = (SavedEntity) metaObject.getOriginalObject();
        if (entity.getCreateTime() == null) {
            this.setFieldValByName(UpdatedEntity.CREATE_TIME, LocalDateTime.now(), metaObject);
        }
        if (entity.getCreateUserId() == null || entity.getCreateUserId().equals(0)) {
            Object userIdVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(SavedEntity.CREATE_USER_ID)
                    .getName()) ? String.valueOf(ContextUtils.getUserId()) : ContextUtils.getUserId();
            this.setFieldValByName(UpdatedEntity.CREATE_USER_ID, userIdVal, metaObject);
        }
    }


    private void fillUpdated(MetaObject metaObject) {
        // 修改人 修改时间
        if (metaObject.getOriginalObject() instanceof UpdatedEntity) {
            update(metaObject);
            return;
        }

        if (metaObject.hasGetter(UpdatedEntity.UPDATE_USER_ID)) {
            Object oldVal = metaObject.getValue(UpdatedEntity.UPDATE_USER_ID);
            if (oldVal == null) {
                this.setFieldValByName(UpdatedEntity.UPDATE_USER_ID, ContextUtils.getUserId(), metaObject);
            }
        }
        if (metaObject.hasGetter(UpdatedEntity.UPDATE_TIME)) {
            Object oldVal = metaObject.getValue(UpdatedEntity.UPDATE_TIME);
            if (oldVal == null) {
                this.setFieldValByName(UpdatedEntity.UPDATE_TIME, LocalDateTime.now(), metaObject);
            }
        }
    }

    private void update(MetaObject metaObject) {
        UpdatedEntity entity = (UpdatedEntity) metaObject.getOriginalObject();
        if (entity.getUpdateTime() == null || entity.getUpdateUserId().equals(0)) {
            Object userIdVal = StrPool.STRING_TYPE_NAME.equals(
                    metaObject.getGetterType(UpdatedEntity.UPDATE_USER_ID).getName())
                    ? String.valueOf(ContextUtils.getUserId()) : ContextUtils.getUserId();
            this.setFieldValByName(UpdatedEntity.UPDATE_USER_ID, userIdVal, metaObject);
        }
        if (entity.getUpdateTime() == null) {
            this.setFieldValByName(UpdatedEntity.UPDATE_TIME, LocalDateTime.now(), metaObject);
        }
    }

    /**
     * 所有的继承了 Entity、SuperEntity 的实体，在 update 时，
     * updateUserId: 自动赋予 当前线程上的登录人id
     * updateTime: 自动赋予 服务器的当前时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        fillUpdated(metaObject);
    }
}
