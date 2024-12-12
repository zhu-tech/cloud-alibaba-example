package com.neyogoo.example.admin.sys.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.vo.request.user.UserListReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserPageReqVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserListRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserPageRespVO;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户
 */
@Repository
public interface UserMapper extends SuperMapper<User> {

    /**
     * 获取用户分页列表
     */
    IPage<UserPageRespVO> findPage(IPage iPage, @Param("param") UserPageReqVO param);

    /**
     * 查询机构下拥有指定角色的人员id列表
     */
    List<Long> listUserIdsWithOrgIdAndRoleId(@Param("orgId") Long orgId, @Param("roleId") Long roleId);

    /**
     * 根据人员范围查询机构人员
     */
    List<UserListRespVO> listUsersWithScope(@Param("param") UserListReqVO param);

    /**
     * 根据部门id查询机构人员
     */
    List<UserListRespVO> listUsersByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据id列表查询所属部门名称
     */
    List<Pair<Long, String>> listDeptNamesByIds(@Param("userIds") Collection<Long> ids);
}
