package com.neyogoo.example.admin.sys.dao;

import com.neyogoo.example.admin.sys.model.UserDept;
import com.neyogoo.example.admin.sys.vo.response.user.UserDeptRespVO;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户关联部门
 */
@Repository
public interface UserDeptMapper extends SuperMapper<UserDept> {

    /**
     * 根据部门id查询可用用户id
     */
    List<Long> listUsableUserIdsByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据部门id列表查询可用用户id
     */
    List<UserDeptRespVO> listUsableUsersByDeptIds(@Param("deptIds") Collection<Long> deptIds);

    /**
     * 根据用户id列表查询关联部门名称
     */
    List<Pair<Long, String>> listDeptNamesByUserIds(@Param("userIds") Collection<Long> userIds);
}
