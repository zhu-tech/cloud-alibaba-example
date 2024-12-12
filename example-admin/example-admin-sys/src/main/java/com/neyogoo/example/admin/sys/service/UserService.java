package com.neyogoo.example.admin.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.vo.request.user.UserListReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserPageReqVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserListRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserPageRespVO;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
public interface UserService extends SuperService<User> {

    /**
     * 根据手机号查询
     */
    User getByInUseUserMobile(String userMobile);

    /**
     * 根据id列表查询名称
     */
    Map<Long, String> mapNamesByIds(Collection<Long> ids);

    /**
     * 根据id列表查询所属部门名称
     */
    Map<Long, String> mapDeptNamesByIds(Collection<Long> ids);

    /**
     * 根据id列表查询用户简易信息
     */
    List<User> listSimpleInfoByIds(Collection<Long> ids);

    /**
     * 查询机构下拥有指定角色的人员id列表
     */
    List<Long> listUserIdsWithOrgIdAndRoleId(Long orgId, Long roleId);

    /**
     * 查询用户工号是否已存在
     */
    boolean queryIsUserCodeExists(String userCode);

    /**
     * 查询用户手机号是否已存在
     */
    boolean queryIsUserMobileInUsing(Long userId, String userMobile);

    /**
     * 分页
     */
    IPage<UserPageRespVO> findPage(PageReq<UserPageReqVO> pageReq);

    /**
     * 根据人员范围查询机构人员
     */
    List<UserListRespVO> listUserWithScope(UserListReqVO reqVO);

    /**
     * 根据部门id查询机构人员
     */
    List<UserListRespVO> listByDeptId(Long deptId);

    /**
     * 查询是否需要修改密码
     */
    boolean queryIsNeedChangePwd(Long userId);

    /**
     * 根据id启用禁用
     */
    boolean updateUsableById(Long id, Boolean usableFlag);

    /**
     * 根据id更新登录密码
     */
    boolean updateLoginPwdById(Long id, String newPwd);

    /**
     * 根据id锁定
     */
    boolean lockUserById(Long userId, Integer minute);
}
