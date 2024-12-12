package com.neyogoo.example.biz.toolbox.controller;

import com.neyogoo.example.biz.toolbox.service.FileService;
import com.neyogoo.example.biz.toolbox.vo.response.file.FileRespVO;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/v1/file")
@Api(value = "File", tags = "文件")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("获取原文件地址")
    @GetMapping("/get/originalUrl/byId/{id}")
    public R<String> getOriginalUrlById(@PathVariable("id") @NotNull Long id) {
        return R.success(fileService.getOriginalUrlByIdWithCache(id));
    }

    @ApiOperation("获取多个原文件地址")
    @PostMapping("/get/originalUrl/byIds")
    public R<Map<Long, String>> getOriginalUrlByIds(@RequestBody @NotEmpty Collection<Long> ids) {
        return R.success(fileService.listOriginalUrlsByIdsWithCache(ids));
    }

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public R<FileRespVO> uploadFile(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "folder", required = false) String folder) {
        fileService.checkFile(file);
        return R.success(FileRespVO.fromModel(fileService.uploadFile(file, folder)));
    }

    @ApiOperation("替换文件")
    @PostMapping("/replace")
    public R<FileRespVO> replaceFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("fileId") Long fileId,
                                     @RequestParam(value = "folder", required = false) String folder) {
        fileService.checkFile(file);
        return R.success(FileRespVO.fromModel(fileService.replaceFile(file, fileId, folder)));
    }

    @CheckSignature
    @ApiOperation("下载文件")
    @GetMapping("/download/byId/{id}")
    public void downloadById(@PathVariable("id") @NotNull Long id, HttpServletResponse response) {
        fileService.downloadFile(id, response);
    }
}
