package com.yachiyo.controller;


import com.yachiyo.Utils.FileUrlUtil;
import com.yachiyo.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileUrlUtil fileUrlUtil;

    private final String UPLOAD_FILE_PATH = com.yachiyo.Utils.IOFileUtils.UPLOAD_FILE_PATH;

    @GetMapping("/generate/{fileName}")
    public ResponseEntity<Resource> generateFileUrl(@PathVariable String fileName,
                                                    @RequestParam long expire,
                                                    @RequestParam String sign) {
        if (!fileUrlUtil.verify(fileName, expire, sign)){
            return ResponseEntity.status(403).body(null);
        }
        File file = new File(UPLOAD_FILE_PATH + "/" + fileName);
        if (!file.exists()){
            return ResponseEntity.status(404).body(null);
        }

        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(new FileSystemResource(file));
    }
}
