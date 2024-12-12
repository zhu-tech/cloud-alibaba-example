package com.neyogoo.example.biz.toolbox.fallback;

import com.neyogoo.example.biz.toolbox.api.FileApi;
import com.neyogoo.example.biz.toolbox.vo.response.FileRespVO;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class FileApiFallbackFactory implements FallbackFactory<FileApi> {

    @Override
    public FileApi create(Throwable cause) {

        return new FileApi() {

            @Override
            public R<FileRespVO> upload(MultipartFile file) {
                log.error("upload file error, cause = {}", cause.getMessage());
                return R.fail("上传文件失败");
            }

            @Override
            public R<FileRespVO> upload(MultipartFile file, String folder) {
                log.error("upload file error, folder = {}, cause = {}", folder, cause.getMessage());
                return R.fail("上传文件失败");
            }

            @Override
            public R<FileRespVO> replace(MultipartFile file, Long fileId) {
                log.error("replace file error, fileId = {}, cause = {}", fileId, cause.getMessage());
                return R.fail("替换文件失败");
            }

            @Override
            public R<FileRespVO> replace(MultipartFile file, Long fileId, String folder) {
                log.error("replace file error, fileId = {}, folder = {}, cause = {}",
                        fileId, folder, cause.getMessage());
                return R.fail("替换文件失败");
            }

            @Override
            public R<String> getOriginalUrlById(Long id) {
                log.error("get file original url by id error, ids = {}, cause = {}", id, cause.getMessage());
                return R.fail("获取文件地址失败");
            }

            @Override
            public R<Map<Long, String>> getOriginalUrlByIds(Collection<Long> ids) {
                log.error("get file original url by ids error, ids = {}, cause = {}", ids, cause.getMessage());
                return R.fail("获取文件地址列表失败");
            }
        };
    }
}
