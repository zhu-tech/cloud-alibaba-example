package com.neyogoo.example.biz.toolbox.service.impl.logging;


import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.biz.toolbox.model.logging.SysOptLog;
import com.neyogoo.example.biz.toolbox.repository.SysOptLogRepository;
import com.neyogoo.example.biz.toolbox.service.logging.SysOptLogService;
import com.neyogoo.example.biz.toolbox.vo.request.logging.SysOptLogPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.logging.SysOptLogRespVO;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOptLogServiceImpl implements SysOptLogService {

    @Autowired
    private SysOptLogRepository baseRepository;

    /**
     * 分页
     */
    @Override
    public PageResp<SysOptLogRespVO> queryPage(PageReq<SysOptLogPageReqVO> pageReq) {
        // 查询条件
        Query query = pageReq.getModel().toQueryParam();
        // 查询数据总数
        long count = baseRepository.queryCount(query);
        // 查询分页数据
        List<SysOptLog> list = baseRepository.queryPage(query, pageReq);

        PageResp<SysOptLogRespVO> pageResp = new PageResp<>(pageReq.getCurrent(), pageReq.getSize());
        pageResp.setTotal(Integer.parseInt(Long.toString(count)));
        if (CollectionUtil.isNotEmpty(list)) {
            pageResp.setRecords(EntityUtils.toBeanList(list, SysOptLogRespVO.class));
        }
        return pageResp;
    }
}
