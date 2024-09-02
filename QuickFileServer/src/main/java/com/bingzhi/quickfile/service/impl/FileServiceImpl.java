package com.bingzhi.quickfile.service.impl;

import com.bingzhi.quickfile.domain.UploadRequest;
import com.bingzhi.quickfile.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bryan Yang
 * QuickList Service
 */
@Service
public class FileServiceImpl implements FileService {

    private static final String UPLOAD_DIR = "/Users/yangyida/Documents/project/QuiclList/QuickFile/QuiclListServer/src/main/resources/"; // 文件上传目录

    /**
     * todo 问旭哥这个要不要做成灵活的 on tuesday
     * 分片大小
     */
    private static final long CHUNK_SIZE = 5 * 1024 * 1024;

    /**
     * 初始化上传，返回上传ID和已上传的分片信息
     * @param uploadRequest
     * @return
     */
    @Override
    public ResponseEntity<Map<String, Object>> initUpload(@RequestBody UploadRequest uploadRequest){
        String fileMd5 = uploadRequest.getFileMd5(); // 获取文件的MD5值
        String uploadId = UUID.randomUUID().toString(); // 生成唯一的上传ID

        File file = new File(UPLOAD_DIR + uploadId); // 创建上传文件目录
        file.mkdirs();

        // 获取已上传的分片信息
        File[] uploadedChunks = file.listFiles();
        List<Integer> uploadedChunkIndexes = Arrays.stream(uploadedChunks)
                .map(f -> Integer.parseInt(f.getName()))
                .collect(Collectors.toList());


        Map<String, Object> response = new HashMap<>();
        response.put("uploadId", uploadId); // 返回上传ID
        response.put("uploadedChunks", uploadedChunkIndexes); // 返回已上传的分片
        return ResponseEntity.ok(response);
    }
}
