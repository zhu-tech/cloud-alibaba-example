package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.UserDept;
import com.neyogoo.example.admin.sys.vo.response.user.UserDeptRespVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户关联部门
 */
public interface UserDeptService extends SuperService<UserDept> {

    /**
     * 根据部门id查询可用用户id
     */
    List<Long> listUsableUserIdsByDeptId(Long deptId);

    /**
     * 根据部门id列表查询可用用户id
     */
    Map<Long, List<UserDeptRespVO>> mapByDeptIds(Collection<Long> deptIds);

    /**
     * 根据用户id查询部门id
     */
    List<Long> listDeptIdsByUserId(Long userId);

    /**
     * 根据用户id列表查询关联部门名称
     *
     * @return key：用户id，value：关联部门名称
     */
    Map<Long, String> mapDeptNamesByUserIds(List<Long> userIds);

    /**
     * 保存关联关系
     */
    boolean updateRelations(Long userId, Collection<Long> deptIds);

    /**
     * 根据用户id删除
     */
    boolean removeByUserId(Long userId);

    /**
     * 根据部门id删除
     */
    boolean removeByDeptId(Long deptId);
}
