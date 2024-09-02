package com.bingzhi.quickfile.controller;

import com.bingzhi.quickfile.domain.UploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081") // 允许来自指定域名的请求
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private static final String UPLOAD_DIR = "/Users/yangyida/Documents/project/QuiclList/QuickFile/QuiclListServer/src/main/resources/"; // 文件上传目录
    private static final long CHUNK_SIZE = 5 * 1024 * 1024; // 分片大小

    // 初始化上传，返回上传ID和已上传的分片信息
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initUpload(@RequestBody UploadRequest uploadRequest) {
        String fileMd5 = uploadRequest.getFileMd5(); // 获取文件的MD5值
        String uploadId = UUID.randomUUID().toString(); // 生成唯一的上传ID
        File file = new File(UPLOAD_DIR + uploadId); // 创建上传文件目录
        System.err.println("fileMd5"+fileMd5);
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

    // 上传单个分片
    @PostMapping("/chunk")
    public ResponseEntity<String> uploadChunk(@RequestParam String uploadId,
                                              @RequestParam int chunkIndex,
                                              @RequestParam MultipartFile chunk) throws IOException {
        String fileDir = UPLOAD_DIR + uploadId; // 获取上传目录路径
        File dir = new File(fileDir);
        System.err.println(uploadId);
        // 确保目录存在，如果不存在则创建
        if (!dir.exists()) {
            dir.mkdirs(); // 创建目录，包括必要的但不存在的父目录
        }

        File chunkFile = new File(fileDir, String.valueOf(chunkIndex)); // 创建分片文件
        chunk.transferTo(chunkFile); // 保存分片数据到服务器

        return ResponseEntity.ok("Chunk uploaded successfully.");
    }


    // 完成上传，合并所有分片
    @PostMapping("/complete")
    public ResponseEntity<HashMap> completeUpload(@RequestParam("uploadId") String uploadId , @RequestParam("fileName")String fileName) throws IOException {
        System.err.println("uploadId is "+uploadId);
        File dir = new File(UPLOAD_DIR + uploadId); // 获取上传目录
        File[] chunks = dir.listFiles(); // 获取所有分片文件
        Arrays.sort(chunks, Comparator.comparingInt(f -> Integer.parseInt(f.getName()))); // 按分片索引排序

        File aaa = new File(UPLOAD_DIR + "/res/"+ uploadId );
        aaa.mkdir();

        File completeFile = new File(UPLOAD_DIR +"/res/"+ uploadId +"/"+ fileName); // 创建完整文件


        try (RandomAccessFile raf = new RandomAccessFile(completeFile, "rw")) {
            // 依次写入所有分片到完整文件
            for (File chunk : chunks) {
                byte[] chunkData = Files.readAllBytes(chunk.toPath());
                raf.write(chunkData);
                chunk.delete();
            }
        }

        dir.delete();

        HashMap<String,String> res=  new HashMap<>();
        res.put("uploadId",uploadId);
        res.put("fileName",fileName);

        return ResponseEntity.ok(res);
    }
}
