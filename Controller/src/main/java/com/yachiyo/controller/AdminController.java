package com.yachiyo.controller;

import com.yachiyo.result.Result;
import com.yachiyo.service.RAGResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/yachiyo/168/mini/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    @Autowired
    private RAGResourceService ragResourceService;

    @PostMapping("/upload")
    public Result<Boolean> UploadResource(@RequestParam("files") List<MultipartFile> files) {
        return ragResourceService.uploadResource(files);
    }
}
