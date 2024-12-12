package com.neyogoo.example.biz.toolbox.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.neyogoo.example.biz.toolbox.config.properties.FileProperties;
import com.neyogoo.example.biz.toolbox.constant.ToolboxCacheConstant;
import com.neyogoo.example.biz.toolbox.dao.FileMapper;
import com.neyogoo.example.biz.toolbox.model.File;
import com.neyogoo.example.biz.toolbox.service.FileService;
import com.neyogoo.example.biz.toolbox.util.FileUtils;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.database.model.IdEntity;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件
 */
@Slf4j
@Service
public class FileServiceImpl extends SuperServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private CacheOps cacheOps;

    /**
     * 根据id查询文件
     */
    @Override
    public File getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<File>lbQ()
                        .eq(File::getId, id)
        );
    }

    /**
     * 根据id列表
     */
    @Override
    public List<File> listByIds(Collection<? extends Serializable> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<File>lbQ()
                        .in(File::getId, ids)
        );
    }

    /**
     * 根据id获取原文件地址
     */
    @Override
    public String getOriginalUrlByIdWithCache(Long id) {
        if (id == null) {
            return null;
        }
        String s = cacheOps.get(buildCacheKey(id), k -> Optional.ofNullable(getById(id)).map(File::getOriginalUrl)
                .orElse(""), true);
        return StrUtil.isBlank(s) ? null : s;
    }

    /**
     * 根据id获取原文件地址
     */
    @Override
    public Map<Long, String> listOriginalUrlsByIdsWithCache(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<Long> keys = ids.stream().distinct().collect(Collectors.toList());
        // 拼接 keys
        List<CacheKey> cacheKeys = keys.stream().map(this::buildCacheKey).collect(Collectors.toList());
        // 切割
        List<List<CacheKey>> partitionKeys = Lists.partition(cacheKeys, 20);

        List<String> values = partitionKeys.stream().map(ks -> cacheOps.<String>find(ks))
                .flatMap(Collection::stream).collect(Collectors.toList());

        List<Pair<Long, String>> filePairs = new ArrayList<>();

        // 缓存不存在的key
        Set<Long> missIds = Sets.newLinkedHashSet();
        for (int i = 0; i < values.size(); i++) {
            String v = values.get(i);
            Long k = keys.get(i);
            if (v == null) {
                missIds.add(k);
            } else {
                filePairs.add(new Pair<>(k, v));
            }
        }
        // 加载 miss 的数据，并设置到缓存
        if (CollUtil.isNotEmpty(missIds)) {
            Map<Long, String> fileUrlMap = baseMapper.selectList(
                    Wraps.<File>lbQ()
                            .select(File::getId, File::getOriginalUrl)
                            .in(File::getId, missIds)
            ).stream().collect(Collectors.toMap(IdEntity::getId, File::getOriginalUrl));
            for (Long missId : missIds) {
                String fileUrl = fileUrlMap.get(missId);
                if (StrUtil.isNotBlank(fileUrl)) {
                    filePairs.add(new Pair<>(missId, fileUrl));
                } else {
                    fileUrl = "";
                }
                cacheOps.set(buildCacheKey(missId), fileUrl);
            }
        }
        filePairs = filePairs.stream().filter(o -> StrUtil.isNotBlank(o.getValue())).collect(Collectors.toList());
        return Pair.toMap(filePairs);
    }

    /**
     * 文件上传校验
     */
    @Override
    public void checkFile(MultipartFile file) throws BizException {
        String originalFilename = file.getOriginalFilename();
        log.info("upload file name = {}", originalFilename);
        // 文件后缀
        String suffix = FileUtils.getSuffix(originalFilename).replaceFirst(".", "");
        // 如果允许的后缀里不包含后缀名，报错
        if (!fileProperties.getAllowSuffix().contains(suffix)) {
            log.info("upload file suffix = {}", suffix);
            throw new BizException("文件格式不正确");
        }
        long fileSize = file.getSize();
        // 如果允许的后缀里不包含后缀名，报错
        if (fileSize > fileProperties.getAllowSize()) {
            log.info("upload file size = {}", fileSize);
            throw new BizException("文件过大");
        }
    }

    /**
     * 上传文件
     */
    @Override
    @SneakyThrows
    public File uploadFile(MultipartFile file, String folder) {
        return uploadFile(file, folder, null);
    }

    /**
     * 上传文件
     */
    @Override
    @SneakyThrows
    public File uploadFile(MultipartFile file, String folder, Long fileId) {
        // 原文件名称
        String originalFilename = file.getOriginalFilename();
        String path = fileProperties.getBasePath() + java.io.File.separator + ContextUtils.getOrgId();
        // 上传文件, 上传目录为 配置文件基础路径+机构id+yyyyMMdd
        long fileSize = file.getSize();
        String realPath = FileUtils.upload(file, path, folder, originalFilename);
        log.info("upload file real path = {}", realPath);
        // 保存文件存储信息
        File bizFile = new File();
        bizFile.setId(fileId);
        bizFile.setFileSuffix(FileUtils.getSuffix(originalFilename).replaceFirst("\\.", ""));
        bizFile.setUploadName(originalFilename);
        bizFile.setFileSize(fileSize);
        bizFile.setOriginalUrl(realPath);
        saveOrUpdate(bizFile);
        cacheOps.set(buildCacheKey(bizFile.getId()), bizFile.getOriginalUrl());
        return bizFile;
    }

    /**
     * 替换文件
     */
    @Override
    public File replaceFile(MultipartFile file, Long fileId, String folder) {
        File oldFile = getById(fileId);
        if (oldFile != null) {
            deleteFile(oldFile);
        }
        return uploadFile(file, folder, fileId);
    }

    /**
     * 下载文件
     */
    @Override
    @SneakyThrows
    public void downloadFile(Long id, HttpServletResponse response) {
        if (id == null) {
            return;
        }
        File bizFile = getById(id);
        ArgumentAssert.notNull(bizFile, "未查询到该文件记录");

        String filePath = bizFile.getOriginalUrl();
        java.io.File file = new java.io.File(filePath);
        ArgumentAssert.isTrue(file.exists(), "文件不存在或已删除");

        response.setContentType("application/x-msdownload");
        response.addHeader("Content-Disposition", "attachment; filename=\""
                + new String(bizFile.getUploadName().getBytes(), StandardCharsets.ISO_8859_1) + "\"");

        // 文件流
        // SKIP CHECKSTYLE:START
        try (InputStream inStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            // SKIP CHECKSTYLE:END
            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while ((len = inStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 删除文件
     */
    @Override
    public boolean deleteFile(Collection<Long> ids) {
        List<File> files = listByIds(ids);
        if (CollectionUtils.isEmpty(files)) {
            return false;
        }
        files.parallelStream().forEach(this::deleteFile);
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    /**
     * 删除文件
     */
    private boolean deleteFile(File bizFile) {
        String filePath = bizFile.getOriginalUrl();
        if (!filePath.startsWith(fileProperties.getBasePath())) {
            throw BizException.wrap(String.format("该文件不在指定管理路径下，请检查：%s", filePath));
        }
        return FileUtils.delete(filePath);
    }

    /**
     * 构建缓存key
     */
    private CacheKey buildCacheKey(Long id) {
        return new CacheKey(ToolboxCacheConstant.FILE_ORIGINAL_URL + id, Duration.ofHours(8));
    }
}
