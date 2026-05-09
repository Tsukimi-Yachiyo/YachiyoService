package com.yachiyo.ContentService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FeignClient(name = "file-service", path = "/internal/file")
public interface FileClient{


    @GetMapping("/getUrl")
    String getUrl(@RequestParam("url") String url,
                  @RequestParam("time") long time,
                  @RequestParam(value = "prefix", defaultValue = "save") String prefix) ;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    boolean upload(
            @RequestParam("fileName") String fileName,
            @RequestPart(required = false) MultipartFile file);

    @GetMapping("/getNames")
    List<String> getNames(@RequestParam("dirName") String dirName) throws IOException;

    @DeleteMapping("/delete")
    boolean delete(
            @RequestParam("fileName") String fileName);


    @PostMapping("/save")
    boolean save(
            @RequestParam("fileName") String fileName,
            @RequestParam(required = false) MultipartFile file);
}
