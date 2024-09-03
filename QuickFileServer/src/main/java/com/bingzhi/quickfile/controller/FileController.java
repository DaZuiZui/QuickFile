package com.bingzhi.quickfile.controller;

import com.bingzhi.quickfile.domain.UploadRequest;
import com.bingzhi.quickfile.service.FileService;
import com.bingzhi.quickfile.util.FileMd5Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bryan Yang
 * QuickList controlelr
 */
@RestController
@RequestMapping("/quickList")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 初始化文件上传，返回上传ID和已上传的分片信息
     * @param uploadRequest
     * @return
     */
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initUpload(@RequestBody UploadRequest uploadRequest) {
        return fileService.initUpload(uploadRequest);
    }


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
                                              @RequestParam MultipartFile chunk) throws IOException{
        return fileService.uploadChunk(uploadId, chunkIndex, chunk);
    }


    /**
     * 分片合并
     * @param uploadId 唯一标识
     * @param fileName 文件name
     * @param md5      文件加密md5标识
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/complete")
    public ResponseEntity<HashMap> completeUpload(@RequestParam("uploadId") String uploadId , @RequestParam("fileName")String fileName , @RequestParam("md5")String md5) throws IOException, NoSuchAlgorithmException {
        return fileService.completeUpload(uploadId, fileName, md5);
    }
}
