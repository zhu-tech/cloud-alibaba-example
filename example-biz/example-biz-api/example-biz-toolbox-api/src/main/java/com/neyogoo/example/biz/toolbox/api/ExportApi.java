package com.neyogoo.example.biz.toolbox.api;

import com.neyogoo.example.biz.toolbox.fallback.ExportApiFallbackFactory;
import com.neyogoo.example.biz.toolbox.vo.response.ExportRespVO;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导出记录
 */
@FeignClient(name = "example-biz-toolbox", fallbackFactory = ExportApiFallbackFactory.class,
        path = "/v1/export", qualifiers = "exportApi")
public interface ExportApi {

    /**
     * 上传导出文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<ExportRespVO> upload(@RequestPart("file") MultipartFile file,
                           @RequestParam("folder") String folder);
}
