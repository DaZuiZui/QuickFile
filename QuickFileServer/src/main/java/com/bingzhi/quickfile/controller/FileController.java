package com.bingzhi.quickfile.controller;

import com.bingzhi.quickfile.domain.UploadRequest;
import com.bingzhi.quickfile.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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


}
