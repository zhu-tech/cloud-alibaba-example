package com.neyogoo.example.biz.toolbox.util;

import cn.hutool.core.text.CharSequenceUtil;
import com.neyogoo.example.common.core.util.DateUtils;
import com.neyogoo.example.common.core.util.UUidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class FileUtils {

    /**
     * 保存文件到服务器
     *
     * @param file     文件
     * @param path     文件存放路径
     * @param fileName 源文件名
     */
    public static String upload(MultipartFile file, String path, String folder, String fileName) throws IOException {
        // 生成新的文件名
        String realPath = getFilePath(path, folder, fileName);
        File dest = new File(realPath);
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            // 保存文件
            file.transferTo(dest);
            // 设置权限
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.OTHERS_READ);
            Files.setPosixFilePermissions(Paths.get(realPath), perms);
            log.info("文件上传成功，path = {}， fileName = {}，realPath = {}", path, fileName, realPath);
            return realPath;
        } catch (IOException e) {
            log.error("文件上传失败，path = {}， fileName = {}", path, fileName);
            throw e;
        }
    }

    /**
     * 从服务器删除文件
     *
     * @param filePath 文件在服务器上的绝对路径
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        log.info("delete file {}", filePath);
        return file.delete();
    }

    /**
     * 获取文件后缀
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     *
     * @param fileOriginName 源文件名
     */
    public static String getFileName(String fileOriginName) {
        return UUidUtils.uuid() + getSuffix(fileOriginName);
    }

    /**
     * 生成新的文件路径
     *
     * @param path           路径
     * @param fileOriginName 原文件名
     */
    public static String getFilePath(String path, String folder, String fileOriginName) {
        // 生成新的文件名
        String filePath = path + File.separator
                + DateUtils.format(LocalDate.now(), DateUtils.DEFAULT_YEARMONTHDAY_FORMAT) + File.separator;
        if (CharSequenceUtil.isNotBlank(folder)) {
            if (filePath.endsWith(File.separator)) {
                if (folder.startsWith(File.separator)) {
                    filePath = filePath + folder.substring(1) + File.separator;
                } else {
                    filePath = filePath + folder + File.separator;
                }
            } else {
                if (folder.startsWith(File.separator)) {
                    filePath = filePath + folder + File.separator;
                } else {
                    filePath = filePath + File.separator + folder + File.separator;
                }
            }
        }
        filePath = filePath + getFileName(fileOriginName);
        return filePath;
    }
}
