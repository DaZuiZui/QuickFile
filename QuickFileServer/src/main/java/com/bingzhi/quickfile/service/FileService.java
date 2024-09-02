package com.bingzhi.quickfile.service;

import com.bingzhi.quickfile.domain.UploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Bryan Yang
 * QuickFile service inteface
 */
public interface FileService {
    /**
     * 初始化上传，返回上传ID和已上传的分片信息
     * @param uploadRequest
     * @return
     */
    public ResponseEntity<Map<String, Object>> initUpload(@RequestBody UploadRequest uploadRequest);
}
