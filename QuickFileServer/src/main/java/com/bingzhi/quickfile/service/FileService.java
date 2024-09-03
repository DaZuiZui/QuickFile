package com.bingzhi.quickfile.service;

import com.bingzhi.quickfile.domain.UploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

    /**
     * 上传单个分片
     * @param uploadId
     * @param chunkIndex
     * @param chunk
     * @return
     * @throws IOException
     */
    @PostMapping("/chunk")
    public ResponseEntity<String> uploadChunk(@RequestParam String uploadId,
                                              @RequestParam int chunkIndex,
                                              @RequestParam MultipartFile chunk) throws IOException;


    /**
     * 分片合并
     * @param uploadId 唯一标识
     * @param fileName 文件name
     * @param md5      文件加密md5标识
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public ResponseEntity<HashMap> completeUpload(@RequestParam("uploadId") String uploadId , @RequestParam("fileName")String fileName , @RequestParam("md5")String md5) throws IOException, NoSuchAlgorithmException;
}
