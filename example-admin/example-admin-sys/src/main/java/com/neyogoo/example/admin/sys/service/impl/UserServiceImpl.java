package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.dao.UserMapper;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.vo.request.user.UserListReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserPageReqVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserListRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserPageRespVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import com.neyogoo.example.common.database.mybatis.typehandler.EncryptTypeHandler;
import com.neyogoo.example.common.database.util.SqlPageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户
 */
@Slf4j
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private EncryptTypeHandler encryptTypeHandler;

    /**
     * 根据id查询
     */
    @Override
    public User getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return decrypt(baseMapper.selectOne(
                Wraps.<User>lbQ()
                        .eq(User::getId, id)
                        .eq(User::getDeleteFlag, false)
        ));
    }

    /**
     * 根据手机号查询
     */
    @Override
    public User getByInUseUserMobile(String userMobile) {
        if (StringUtils.isBlank(userMobile)) {
            return null;
        }
        return decrypt(baseMapper.selectOne(
                Wraps.<User>lbQ()
                        .eq(User::getUserMobile, encryptTypeHandler.encrypt(userMobile))
                        .eq(User::getUsableFlag, true)
                        .eq(User::getDeleteFlag, false)
        ));
    }

    /**
     * 根据id列表查询名称
     */
    @Override
    public Map<Long, String> mapNamesByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<Long, String> map = Maps.newHashMapWithExpectedSize(ids.size());
        EntityUtils.batchConsume(new ArrayList<>(ids), 50, subIds -> {
            baseMapper.selectList(
                    Wraps.<User>lbQ()
                            .select(User::getId, User::getUserName)
                            .in(User::getId, subIds)
            ).forEach(model -> map.put(model.getId(), model.getUserName()));
        });
        return map;
    }

    /**
     * 根据id列表查询所属部门名称
     */
    @Override
    public Map<Long, String> mapDeptNamesByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return Pair.toMap(baseMapper.listDeptNamesByIds(ids));
    }

    /**
     * 根据id列表查询用户简易信息
     */
    @Override
    public List<User> listSimpleInfoByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<User>lbQ()
                        .select(User::getId, User::getUserName, User::getUserCode, User::getUserMobile)
                        .in(User::getId, ids)
        ).stream().map(this::decrypt).collect(Collectors.toList());
    }

    /**
     * 根据id列表查询
     */
    @Override
    public List<User> listByIds(Collection<? extends Serializable> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<User>lbQ()
                        .in(User::getId, ids)
                        .eq(User::getDeleteFlag, false)
        ).stream().map(this::decrypt).collect(Collectors.toList());
    }

    /**
     * 查询机构下拥有指定角色的人员id列表
     */
    @Override
    public List<Long> listUserIdsWithOrgIdAndRoleId(Long orgId, Long roleId) {
        if (orgId == null || roleId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUserIdsWithOrgIdAndRoleId(orgId, roleId);
    }

    /**
     * 查询用户工号是否已存在
     */
    @Override
    public boolean queryIsUserCodeExists(String userCode) {
        if (StringUtils.isBlank(userCode)) {
            return false;
        }
        return baseMapper.selectOne(
                Wraps.<User>lbQ()
                        .select(User::getId)
                        .eq(User::getUserCode, userCode)
                        .last("limit 1")

        ) != null;
    }

    /**
     * 查询用户手机号是否已存在
     */
    @Override
    public boolean queryIsUserMobileInUsing(Long userId, String userMobile) {
        if (StringUtils.isBlank(userMobile)) {
            return false;
        }
        List<Long> userIds = baseMapper.selectList(
                Wraps.<User>lbQ()
                        .select(User::getId)
                        .eq(User::getUserMobile, encryptTypeHandler.encrypt(userMobile))
                        .eq(User::getUsableFlag, true)
                        .eq(User::getDeleteFlag, false)
        ).stream().map(User::getId).collect(Collectors.toList());

        if (CollUtil.isEmpty(userIds)) {
            return false;
        }
        if (userIds.size() > 1) {
            return true;
        }
        if (userId == null) {
            return true;
        } else {
            return !userIds.get(0).equals(userId);
        }
    }

    /**
     * 分页
     */
    @Override
    public IPage<UserPageRespVO> findPage(PageReq<UserPageReqVO> pageReq) {
        return baseMapper.findPage(SqlPageUtils.buildPage(pageReq), pageReq.getModel());
    }

    /**
     * 根据人员范围查询机构人员
     */
    @Override
    public List<UserListRespVO> listUserWithScope(UserListReqVO reqVO) {
        if (CollUtil.isEmpty(reqVO.getUserIds())) {
            return Collections.emptyList();
        }
        return baseMapper.listUsersWithScope(reqVO);
    }

    /**
     * 根据部门id查询机构人员
     */
    @Override
    public List<UserListRespVO> listByDeptId(Long deptId) {
        if (deptId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsersByDeptId(deptId);
    }

    /**
     * 查询是否需要修改密码
     */
    @Override
    public boolean queryIsNeedChangePwd(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        String defaultPwd = LoginProperties.doubleEncodePwd(loginProperties.getDefaultPwd(), user.getLoginSalt());
        return user.getLoginPwd().equals(defaultPwd);
    }

    /**
     * 根据id启用禁用
     */
    @Override
    public boolean updateUsableById(Long id, Boolean usableFlag) {
        if (id == null || usableFlag == null) {
            return false;
        }
        if (usableFlag) {
            User user = getById(id);
            if (user == null || user.getUsableFlag()) {
                return false;
            }
            ArgumentAssert.isNull(getByInUseUserMobile(user.getUserMobile()), "该手机号已被其他用户使用");
        }
        return update(
                Wraps.<User>lbU()
                        .set(User::getUsableFlag, usableFlag)
                        .set(User::getCreateUserId, ContextUtils.getUserId())
                        .set(User::getCreateTime, LocalDateTime.now())
                        .eq(User::getId, id)
                        .eq(User::getUsableFlag, !usableFlag)
                        .eq(User::getDeleteFlag, false)
        );
    }

    /**
     * 根据id更新登录密码
     */
    @Override
    public boolean updateLoginPwdById(Long id, String newPwd) {
        if (id == null || StringUtils.isBlank(newPwd)) {
            return false;
        }
        return update(
                Wraps.<User>lbU()
                        .set(User::getLoginPwd, newPwd)
                        .set(User::getUpdateUserId, ContextUtils.getUserId())
                        .set(User::getUpdateTime, LocalDateTime.now())
                        .eq(User::getId, id)
                        .eq(User::getDeleteFlag, false)
        );
    }

    /**
     * 根据id锁定
     */
    @Override
    public boolean lockUserById(Long userId, Integer minute) {
        if (userId == null || minute == null || minute <= 0) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return update(
                Wraps.<User>lbU()
                        .set(User::getLockEndTime, now.plusMinutes(minute))
                        .set(User::getUpdateTime, now)
                        .eq(User::getId, userId)
        );
    }

    /**
     * 根据id删除
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        return update(
                Wraps.<User>lbU()
                        .set(User::getDeleteFlag, true)
                        .set(User::getCreateUserId, ContextUtils.getUserId())
                        .set(User::getCreateTime, LocalDateTime.now())
                        .eq(User::getId, id)
                        .eq(User::getDeleteFlag, false)
        );
    }

    private User decrypt(User user) {
        if (user != null) {
            user.setUserMobile(encryptTypeHandler.decrypt(user.getUserMobile()));
            user.setUserEmail(encryptTypeHandler.decrypt(user.getUserEmail()));
        }
        return user;
    }
}
