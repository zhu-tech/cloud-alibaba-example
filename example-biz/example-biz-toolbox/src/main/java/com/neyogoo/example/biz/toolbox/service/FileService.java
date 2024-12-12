package com.neyogoo.example.biz.toolbox.service;

import com.neyogoo.example.biz.toolbox.model.File;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.database.mvc.service.SuperService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 文件 服务类
 */
public interface FileService extends SuperService<File> {

    /**
     * 根据id获取原文件地址（缓存）
     */
    String getOriginalUrlByIdWithCache(Long id);

    /**
     * 根据id获取原文件地址（缓存）
     */
    Map<Long, String> listOriginalUrlsByIdsWithCache(Collection<Long> ids);

    /**
     * 校验
     */
    void checkFile(MultipartFile file) throws BizException;

    /**
     * 上传
     */
    File uploadFile(MultipartFile file, String folder);

    /**
     * 上传
     */
    File uploadFile(MultipartFile file, String folder, Long fileId);

    /**
     * 替换
     */
    File replaceFile(MultipartFile file, Long fileId, String folder);

    /**
     * 下载
     */
    void downloadFile(Long id, HttpServletResponse response);

    /**
     * 删除
     */
    boolean deleteFile(Collection<Long> ids);
}
