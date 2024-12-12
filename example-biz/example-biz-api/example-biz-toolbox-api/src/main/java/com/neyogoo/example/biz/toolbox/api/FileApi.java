package com.neyogoo.example.biz.toolbox.api;

import com.neyogoo.example.biz.toolbox.fallback.FileApiFallbackFactory;
import com.neyogoo.example.biz.toolbox.vo.response.FileRespVO;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

/**
 * 文件
 */
@FeignClient(name = "example-biz-toolbox", fallbackFactory = FileApiFallbackFactory.class,
        path = "/v1/file", qualifiers = "fileApi")
public interface FileApi {

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<FileRespVO> upload(@RequestPart("file") MultipartFile file);

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<FileRespVO> upload(@RequestPart("file") MultipartFile file,
                         @RequestParam("folder") String folder);

    /**
     * 替换文件
     */
    @PostMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<FileRespVO> replace(@RequestPart("file") MultipartFile file,
                          @RequestParam("fileId") Long fileId);

    /**
     * 替换文件
     */
    @PostMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<FileRespVO> replace(@RequestPart("file") MultipartFile file,
                          @RequestParam("fileId") Long fileId,
                          @RequestParam("folder") String folder);

    /**
     * 获取原文件地址
     */
    @GetMapping("/get/originalUrl/byId/{id}")
    R<String> getOriginalUrlById(@PathVariable("id") @NotNull Long id);

    /**
     * 获取多个原文件地址
     */
    @PostMapping("/get/originalUrl/byIds")
    R<Map<Long, String>> getOriginalUrlByIds(@RequestBody @NotEmpty Collection<Long> ids);
}
