package com.neyogoo.example.biz.toolbox.service.impl.msg;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import com.neyogoo.example.biz.toolbox.repository.MailMsgRepository;
import com.neyogoo.example.biz.toolbox.service.msg.MailMsgService;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgRemindReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgSaveReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.msg.MailMsgRespVO;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailMsgServiceImpl implements MailMsgService {

    @Autowired
    private MailMsgRepository baseRepository;

    /**
     * 查询站内信列表--分页
     */
    @Override
    public PageResp<MailMsgRespVO> queryPage(PageReq<MailMsgPageReqVO> pageReq) {
        // 获取查询条件
        Query query = pageReq.getModel().toQueryParam();
        // 查询数据总数
        long count = baseRepository.queryCount(query);
        // 查询分页数据
        List<MailMsg> list = baseRepository.queryPage(query, pageReq);
        PageResp<MailMsgRespVO> pageResp = new PageResp<>(pageReq.getCurrent(), pageReq.getSize());
        pageResp.setTotal(Integer.parseInt(Long.toString(count)));
        if (CollectionUtil.isNotEmpty(list)) {
            pageResp.setRecords(EntityUtils.toBeanList(list, MailMsgRespVO.class));
        }
        return pageResp;
    }

    /**
     * 根据id查询站内信
     */
    @Override
    public MailMsg getById(String id) {
        return baseRepository.queryById(id);
    }

    /**
     * 查询站内信消息提醒
     */
    @Override
    public List<MailMsg> queryMsgReminder(MailMsgRemindReqVO reqVO) {
        return baseRepository.queryReminder(reqVO.toQueryParam());
    }

    /**
     * 批量保存站内信
     */
    @Override
    public boolean saveBatch(MailMsgSaveReqVO saveReqVO) {
        EntityUtils.batchConsume(saveReqVO.toModels(), 200, models -> baseRepository.insert(models));
        return true;
    }

    /**
     * 根据id列表批量修改为已读
     */
    @Override
    public boolean updateReadAllByIds(List<String> ids) {
        return baseRepository.updateMsgByIds(ids);
    }

    /**
     * 根据id修改为已读
     */
    @Override
    public boolean updateReadOneById(String id) {
        return baseRepository.updateMsgById(id);
    }

    /**
     * 根据id删除站内信
     */
    @Override
    public boolean deleteByIds(List<String> ids) {
        return baseRepository.deleteMsgByIds(ids);
    }

}
