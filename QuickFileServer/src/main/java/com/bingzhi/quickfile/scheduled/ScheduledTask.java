package com.bingzhi.quickfile.scheduled;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
@EnableScheduling
public class ScheduledTask {

    private static final Logger logger = Logger.getLogger(ScheduledTask.class.getName());
    private static final String UPLOAD_DIR = "/Users/yangyida/Documents/project/QuickFile/QuickFileServer/src/main/resources/tmp/"; // 文件上传目录

    // 每分钟执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void performTask() {
        logger.info("任务执行时间：" + new Date());

        // 获取UPLOAD_DIR下的文件和文件夹
        File dir = new File(UPLOAD_DIR);
        if (dir.exists() && dir.isDirectory()) {
            File[] filesList = dir.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    deleteOldFilesAndDirectories(file); // 处理子文件和文件夹
                }
            } else {
                logger.warning("UPLOAD_DIR目录为空");
            }
        } else {
            logger.warning("目录不存在: " + UPLOAD_DIR);
        }
    }

    // 递归删除超过1分钟的文件和空文件夹
    private void deleteOldFilesAndDirectories(File file) {
        // 如果是目录，递归处理其内容
        if (file.isDirectory()) {
            File[] filesList = file.listFiles();
            if (filesList != null) {
                for (File childFile : filesList) {
                    deleteOldFilesAndDirectories(childFile); // 递归调用
                }
            }
            // 删除空目录
            if (file.list().length == 0) {
                deleteIfOlderThanOneMinute(file);
            }
        } else {
            // 如果是文件，直接处理
            deleteIfOlderThanOneMinute(file);
        }
    }

    // 删除创建时间超过1分钟的文件或文件夹
    private void deleteIfOlderThanOneMinute(File file) {
        try {
            // 获取文件或文件夹的创建时间
            BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getAbsolutePath()), BasicFileAttributes.class);
            Date creationTime = new Date(attr.creationTime().toMillis());
            logger.info("检查文件或文件夹: " + file.getName() + " 创建时间: " + creationTime);

            // 获取当前时间
            Date currentTime = new Date();

            // 计算时间差（以分钟为单位）
            long diffInMillis = currentTime.getTime() - creationTime.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

            // 如果创建时间超过1分钟，则删除
            if (diffInMinutes >= 1440) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info("已删除: " + file.getName());
                } else {
                    logger.warning("删除失败: " + file.getName());
                }
            }

        } catch (Exception e) {
            logger.severe("处理文件时发生错误: " + file.getName() + "，错误信息: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
