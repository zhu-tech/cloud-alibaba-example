package com.neyogoo.example.biz.toolbox.service.logging;

import com.neyogoo.example.biz.toolbox.vo.request.logging.SysOptLogPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.logging.SysOptLogRespVO;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;

public interface SysOptLogService {

    /**
     * 分页
     */
    PageResp<SysOptLogRespVO> queryPage(PageReq<SysOptLogPageReqVO> pageReq);

}
