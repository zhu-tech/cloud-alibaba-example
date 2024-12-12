package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.vo.request.org.OrgSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.org.OrgUpdateReqVO;

/**
 * 机构操作
 */
public interface OrgOperationService {

    /**
     * 新增
     */
    Long save(OrgSaveReqVO reqVO);

    /**
     * 修改
     */
    boolean update(OrgUpdateReqVO reqVO);

    /**
     * 启用禁用
     */
    boolean updateUsableById(Long id, Boolean usable);

    /**
     * 删除
     */
    boolean removeById(Long id);
}
