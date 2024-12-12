package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.vo.request.user.UserSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserUpdatePwdReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserUpdateReqVO;

/**
 * 用户操作
 */
public interface UserOperationService {

    /**
     * 新增
     */
    Long save(UserSaveReqVO reqVO);

    /**
     * 修改
     */
    boolean update(UserUpdateReqVO reqVO);

    /**
     * 启用禁用
     */
    boolean updateUsableById(Long userId, Boolean usable);

    /**
     * 更新密码
     */
    boolean updateLoginPwdById(Long userId, UserUpdatePwdReqVO reqVO);

    /**
     * 重置密码
     */
    boolean resetLoginPwdById(Long userId);

    /**
     * 删除
     */
    boolean removeById(Long userId);
}
