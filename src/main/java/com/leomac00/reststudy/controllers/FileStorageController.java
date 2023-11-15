package com.leomac00.reststudy.controllers;

import com.leomac00.reststudy.data.vo.v1.UploadFileResponseVO;
import com.leomac00.reststudy.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/file/v1") // Creates a route to "localhost:8080/person"
@Tag(name = "File Storage", description = "Endpoints for managing file in storage")
public class FileStorageController {
    @Autowired
    private FileStorageService service;

    @PostMapping("/upload/single")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(uploadAndGetResponseVO(file));
    }

    @PostMapping("/upload/many")
    public ResponseEntity<List<UploadFileResponseVO>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        var fileNames = Arrays.asList(files)
                .stream().map(file -> {
                    return uploadAndGetResponseVO(file);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(fileNames);
    }

    @GetMapping("/download/{fileName}")
    public Resource downloadFile(@PathVariable("fileName") String fileName) {

        return service.downloadFile(fileName);
    }

    @GetMapping("/download/listAllFiles")
    public List<String> downloadAllFiles() {
        return service.listAllFiles();
    }

    private UploadFileResponseVO createResponseVO(MultipartFile file, String Uri) {
        var response = new UploadFileResponseVO(
                file.getName(),
                StringUtils.cleanPath(file.getOriginalFilename()),
                file.getContentType(),
                file.getSize()
        );
        return response;
    }

    private UploadFileResponseVO uploadAndGetResponseVO(MultipartFile file) {
        var fileName = service.uploadFile(file);
        var fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/file/v1/downloadFile/")
                .path(fileName)
                .toUriString();
        return createResponseVO(file, fileUri);
    }
}
