package com.neyogoo.example.biz.toolbox.fallback;

import com.neyogoo.example.biz.toolbox.api.ExportApi;
import com.neyogoo.example.biz.toolbox.vo.response.ExportRespVO;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ExportApiFallbackFactory implements FallbackFactory<ExportApi> {

    @Override
    public ExportApi create(Throwable cause) {

        return new ExportApi() {

            @Override
            public R<ExportRespVO> upload(MultipartFile file, String folder) {
                log.error("upload export error, folder = {}, cause = {}", folder, cause.getMessage());
                return R.fail("上传导出文件失败");
            }
        };
    }
}
