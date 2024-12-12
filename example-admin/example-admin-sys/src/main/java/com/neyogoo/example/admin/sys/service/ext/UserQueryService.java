package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.vo.request.user.UserPageReqVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserPageRespVO;
import com.neyogoo.example.common.core.model.DataScope;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.model.Pair;

import java.util.List;

/**
 * 用户查询
 */
public interface UserQueryService {

    /**
     * 根据用户id查询用户信息
     */
    UserGetRespVO getById(Long id);

    /**
     * 查询当前登录人数据权限
     */
    DataScope getDataScope();

    /**
     * 根据用户id和机构id查询数据权限
     */
    DataScope getDataScope(Long userId, Long orgId);

    /**
     * 根据部门id查询
     */
    List<User> listByDeptId(Long deptId);

    /**
     * 根据机构id查询机构管理员
     */
    List<User> listOrgAdminsByOrgId(Long orgId);

    /**
     * 分页
     */
    PageResp<UserPageRespVO> findPage(PageReq<UserPageReqVO> pageReq);

    /**
     * 根据机构id查询下拉框
     */
    List<Pair<Long, String>> getComboBoxByOrgId(Long orgId);

    /**
     * 自动生成用户工号
     */
    String generateUserCode();
}
