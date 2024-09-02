package com.bingzhi.quickfile.domain;

public class UploadRequest {
    private String fileName; // 文件名
    private long fileSize; // 文件大小
    private String fileMd5; // 文件的MD5哈希值

    // 获取文件名
    public String getFileName() {
        return fileName;
    }

    // 设置文件名
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // 获取文件大小
    public long getFileSize() {
        return fileSize;
    }

    // 设置文件大小
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    // 获取文件MD5值
    public String getFileMd5() {
        return fileMd5;
    }

    // 设置文件MD5值
    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}
