package com.neyogoo.example.common.boot.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class SignUtils {

    /**
     * 验签 Md5(Md5(Md5(参数) + nonce) + timestamp)
     */
    public static boolean checkSign(SignParam signParam) {
        StringBuilder builder = new StringBuilder();
        if (CollUtil.isNotEmpty(signParam.pathVariables)) {
            builder.append(String.join(",", signParam.pathVariables));
        }
        if (CollUtil.isNotEmpty(signParam.paramsMap)) {
            builder.append(JsonUtils.toJson(signParam.paramsMap));
        }
        if (StringUtils.isNotBlank(signParam.requestBody)) {
            builder.append(signParam.requestBody);
        }
        String param = builder.toString();
        String sign1 = StringUtils.isBlank(param) ? StrPool.EMPTY : SecureUtil.md5(param);
        String sign2 = SecureUtil.md5(sign1 + signParam.nonce);
        String sign3 = SecureUtil.md5(sign2 + signParam.timestamp);
        boolean flag = signParam.sign.equalsIgnoreCase(sign3);
        if (!flag) {
            log.warn("check signature error, sign = {}, param = {}, sign1 = {}, sign2 = {}, sign3 = {}",
                    signParam.sign, param, sign1, sign2, sign3);
        }
        return flag;
    }

    @Setter
    public static final class SignParam {
        /**
         * PathVariable参数 已排序
         */
        private List<String> pathVariables;
        /**
         * RequestParams参数 已排序
         */
        private Map<String, String> paramsMap;
        /**
         * RequestBody参数
         */
        private String requestBody;
        /**
         * sign签名
         */
        private String sign;
        /**
         * 随机字符串
         */
        private String nonce;
        /**
         * 时间
         */
        private String timestamp;
    }
}
