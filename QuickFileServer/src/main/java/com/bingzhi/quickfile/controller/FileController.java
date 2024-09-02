package com.bingzhi.quickfile.controller;

import com.bingzhi.quickfile.domain.UploadRequest;
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

    // 初始化上传，返回上传ID和已上传的分片信息
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initUpload(@RequestBody UploadRequest uploadRequest) {
        return null;
    }


}
