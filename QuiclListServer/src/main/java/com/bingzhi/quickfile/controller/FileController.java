package com.bingzhi.quickfile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Bryan Yang
 * QuickList controlelr
 */
@RestController
@RequestMapping("/quickList")
public class FileController {
    private static final String UPLOAD_DIR = "/uploads/";


    /**
     * md5 数据
     * @param file
     * @param chunkIndex
     * @param totalChunks
     * @param identifier
     * @return
     * @throws IOException
     * @throws IOException
     */
    @PostMapping("/chunk")
    public ResponseEntity<?> uploadChunk(@RequestParam("file") MultipartFile file,
                                         @RequestParam("chunkIndex") int chunkIndex,
                                         @RequestParam("totalChunks") int totalChunks,
                                         @RequestParam("identifier") String identifier) throws IOException, IOException {

        // 上传路径
        String uploadFilePath = UPLOAD_DIR + identifier + "/";
        File dir = new File(uploadFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        /**
         * 把一个文件拆分为n
         */

        // 保存当前分片
        String chunkFileName = uploadFilePath + chunkIndex;
        File chunkFile = new File(chunkFileName);
        file.transferTo(chunkFile);

        // 检查是否所有分片已上传完毕
        if (chunkIndex == totalChunks - 1) {
            mergeChunks(identifier, totalChunks, uploadFilePath);
        }

        return ResponseEntity.ok().body("Chunk uploaded successfully");
    }

    private void mergeChunks(String identifier, int totalChunks, String uploadFilePath) throws IOException {
        File mergedFile = new File(UPLOAD_DIR + identifier);
        try (FileOutputStream out = new FileOutputStream(mergedFile)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunkFile = new File(uploadFilePath + i);
                try (FileInputStream in = new FileInputStream(chunkFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                chunkFile.delete(); // 删除已合并的分片
            }
        }
        // 删除分片目录
        new File(uploadFilePath).delete();
    }
}
