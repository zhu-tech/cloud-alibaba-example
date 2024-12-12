package com.neyogoo.example.common.database.mybatis.typehandler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AES 加解密
 */
@Slf4j
@Alias("aesEncrypt")
@MappedTypes(CharSequence.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptTypeHandler extends BaseTypeHandler<CharSequence> {

    private AES aes;

    public EncryptTypeHandler(Boolean enabled, String aesKey) {
        this.aes = (!enabled || StringUtils.isBlank(aesKey)) ? null : new AES(aesKey.getBytes());
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CharSequence parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, encrypt((String) parameter));
    }

    @Override
    public CharSequence getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decrypt(rs.getString(columnName));
    }

    @Override
    public CharSequence getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decrypt(rs.getString(columnIndex));
    }

    @Override
    public CharSequence getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decrypt(cs.getString(columnIndex));
    }

    /**
     * 加密
     */
    @SneakyThrows
    public String encrypt(String content) {
        if (aes == null || StringUtils.isBlank(content)) {
            return content;
        }
        return aes.encryptBase64(content);
    }

    /**
     * 解密
     */
    @SneakyThrows
    public String decrypt(String content) {
        if (aes == null || StringUtils.isBlank(content)) {
            return content;
        }
        return StrUtil.str(aes.decrypt(Base64.decode(content)), StandardCharsets.UTF_8);
    }
}