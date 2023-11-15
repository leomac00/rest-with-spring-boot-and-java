package com.leomac00.reststudy.services;

import com.leomac00.reststudy.config.FileStorageConfig;
import com.leomac00.reststudy.data.vo.v1.UploadFileResponseVO;
import com.leomac00.reststudy.exceptions.FileNotFoundException;
import com.leomac00.reststudy.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        var path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create directory to store the files!!!");
        }
    }

    public String uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("File has invalid name policy['..'], please rename and try again");
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new FileStorageException("Could not upload file: " + fileName + " !!!", e.getCause());
        }
    }

    public Resource downloadFile(String fileName) {

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("File has invalid name policy['..'], please rename and try again");
            }
            var pathToFile = this.fileStorageLocation.resolve(StringUtils.cleanPath(fileName));
            var file = new UrlResource(pathToFile.toUri());

            if (file.exists()) {
                return file;
            } else {
                throw new FileNotFoundException(fileName + " does not exist in the folder.");
            }
        } catch (Exception e) {
            throw new FileStorageException("Could download file: " + fileName + " !!!", e.getCause());
        }
    }

    public List<String> listAllFiles() {

        try {
            var fileNames = Stream.of(new File(fileStorageLocation.toUri()).listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .collect(Collectors.toList());
            return fileNames;

        } catch (Exception e) {
            throw new FileStorageException("Could download all files!!!", e.getCause());
        }
    }
}
