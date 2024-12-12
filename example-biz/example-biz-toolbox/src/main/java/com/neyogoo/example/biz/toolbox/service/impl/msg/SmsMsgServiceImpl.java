package com.neyogoo.example.biz.toolbox.service.impl.msg;

import cn.hutool.core.bean.BeanUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.neyogoo.example.biz.common.enumeration.toolbox.SmsTemplateEnum;
import com.neyogoo.example.biz.toolbox.config.properties.SmsProperties;
import com.neyogoo.example.biz.toolbox.model.msg.SmsMsg;
import com.neyogoo.example.biz.toolbox.repository.SmsMsgRepository;
import com.neyogoo.example.biz.toolbox.service.msg.SmsMsgService;
import com.neyogoo.example.biz.toolbox.vo.request.msg.SmsSendReqVO;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.core.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class SmsMsgServiceImpl implements SmsMsgService {

    @Autowired
    private SmsProperties smsProperties;
    @Autowired
    private SmsMsgRepository smsSendRepository;

    /**
     * 发送短信（批量）
     */
    @Override
    @SneakyThrows
    public boolean sendBatchMessage(SmsSendReqVO reqVO) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAccessKeyId(),
                smsProperties.getAccessKeySecret());
        DefaultProfile.addEndpoint(smsProperties.getRegionId(), smsProperties.getProduct(), smsProperties.getDomain());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 验证参数
        validateParam(reqVO.getParams(), reqVO.getTemplateCode());
        // 发送短信
        EntityUtils.batchConsume(reqVO.getPhoneNumbers(), 200, phoneNumbers -> {
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(String.join(",", phoneNumbers));
            request.setSignName(smsProperties.getSignName());
            request.setTemplateCode(reqVO.getTemplateCode());
            request.setTemplateParam(JsonUtils.toJson(reqVO.getParams()));
            SendSmsResponse response = null;
            try {
                response = acsClient.getAcsResponse(request);
            } catch (ClientException e) {
                log.error("发送短信，reqVO = {}，phones = {}, error：{}", JsonUtils.toJson(reqVO),
                        JsonUtils.toJson(phoneNumbers), JsonUtils.toJson(e.getMessage()));
            }
            // 保存短信响应信息
            log.info("发送短信，reqVO = {}，response：{}", JsonUtils.toJson(reqVO), JsonUtils.toJson(response));
            smsSendRepository.insert(reqVO.toModels(response));
        });

        return true;
    }

    /**
     * 发送短信
     */
    @Override
    @SneakyThrows
    public boolean sendMessage(SmsSendReqVO reqVO) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", smsProperties.getAccessKeyId(),
                smsProperties.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-beijing", smsProperties.getProduct(), smsProperties.getDomain());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 验证参数
        validateParam(reqVO.getParams(), reqVO.getTemplateCode());
        // 发送短信
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(reqVO.getPhoneNumbers().get(0));
        request.setSignName(smsProperties.getSignName());
        request.setTemplateCode(reqVO.getTemplateCode());
        request.setTemplateParam(JsonUtils.toJson(reqVO.getParams()));
        SendSmsResponse response = acsClient.getAcsResponse(request);
        // 保存短信响应信息
        SmsMsg smsSendMsg = reqVO.toModel();
        log.info("发送短信，reqVO = {}，response：{}", JsonUtils.toJson(reqVO), JsonUtils.toJson(response));
        smsSendMsg.setSendSmsResponse(BeanUtil.toBean(response, SmsMsg.SendSmsResponse.class));
        smsSendRepository.insert(smsSendMsg);
        return true;
    }


    /**
     * 校验参数
     */
    private void validateParam(Map<String, Object> templateParam, String templateCode) {
        SmsTemplateEnum byTemplateCode = SmsTemplateEnum.getByTemplateCode(templateCode);
        ArgumentAssert.notNull(byTemplateCode, "短信模板编码不存在");
        String keys = byTemplateCode.getParams();
        String[] keyArr = keys.split(",");
        for (String item : keyArr) {
            if (!templateParam.containsKey(item)) {
                ArgumentAssert.notNull(templateParam, "模板缺少参数：" + item);
            }
        }
    }
}
