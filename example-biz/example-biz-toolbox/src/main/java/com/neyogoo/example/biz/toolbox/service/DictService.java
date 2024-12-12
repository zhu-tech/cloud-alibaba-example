package com.neyogoo.example.biz.toolbox.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.model.Dict;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictMapReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictSaveReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictUpdateReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.dict.DictMapRespVO;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.List;
import java.util.Map;

/**
 * 字典
 */
public interface DictService extends SuperService<Dict> {

    /**
     * 根据类型查询字典列表
     */
    List<Dict> listByType(DictTypeEnum dictType);

    /**
     * 根据类型查询字典列表
     */
    Dict getByTypeAndCode(DictTypeEnum dictType, String dictCode);

    /**
     * 根据类型和是否启用查询字典map
     */
    Map<String, List<DictMapRespVO>> mapByTypes(DictMapReqVO reqVO);

    /**
     * 根据类型查询启用字典map
     */
    Map<String, Map<String, String>> mapUsableByTypes(List<String> dictTypes);

    /**
     * 分页查询字典信息
     */
    IPage<Dict> findPage(PageReq<DictPageReqVO> pageReq);

    /**
     * 新增字典信息
     */
    Dict save(DictSaveReqVO saveReqVO);

    /**
     * 更新字典信息
     */
    Dict update(DictUpdateReqVO updateReqVO);

    /**
     * 根据id启用禁用字典
     */
    boolean updateUsableById(Long id, Boolean usableFlag);

    /**
     * 根据id删除字典信息
     */
    boolean removeById(Long id);


}
