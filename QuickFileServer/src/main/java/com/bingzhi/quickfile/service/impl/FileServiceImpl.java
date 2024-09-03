package com.bingzhi.quickfile.service.impl;

import com.bingzhi.quickfile.domain.UploadRequest;
import com.bingzhi.quickfile.service.FileService;
import com.bingzhi.quickfile.util.FileMd5Calculator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
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

    /**
     * 上传单个分片
     * @param uploadId
     * @param chunkIndex
     * @param chunk
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<String> uploadChunk(@RequestParam String uploadId,
                                              @RequestParam int chunkIndex,
                                              @RequestParam MultipartFile chunk) throws IOException{
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

    /**
     * 分片合并
     * @param uploadId 唯一标识
     * @param fileName 文件name
     * @param md5      文件加密md5标识
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Override
    public ResponseEntity<HashMap> completeUpload(@RequestParam("uploadId") String uploadId , @RequestParam("fileName")String fileName , @RequestParam("md5")String md5) throws IOException, NoSuchAlgorithmException{
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

        String againPassWord = FileMd5Calculator.calculateFileMd5(completeFile);
        if (!md5.equals(againPassWord)) {
            //todo 文件不一致

        }

        HashMap<String,String> res=  new HashMap<>();
        res.put("uploadId",uploadId);
        res.put("fileName",fileName);

        return ResponseEntity.ok(res);
    }
}
