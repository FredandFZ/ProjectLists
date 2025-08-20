package org.example.codesignconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.model.File;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.codesignconnect.utils.AliyunOSSOperator;

import java.util.Objects;

@Slf4j
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @GetMapping("/{id}")
    public Result getFile(@PathVariable Integer id) {
        return Result.success(fileService.getFileById(id));
    }

    @GetMapping("/projects/{projectId}/files")
    public Result getProjectFileList(@PathVariable Integer projectId, @RequestParam Short projectStatus, @RequestParam Integer iterationId) {
        log.info("Get project file list");
        return Result.success(fileService.getFilesInFolder(projectId, projectStatus, iterationId));
    }

    @PostMapping("/projects/files/documents")
    public Result createDocumentFile(@ModelAttribute File file, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception{
        String url = aliyunOSSOperator.upload(multipartFile.getBytes(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.setSource(url);
        return Result.success(fileService.addFile(file));
    }

    @PostMapping("/projects/files/whiteboard")
    public Result createWhiteboardFile(@RequestBody File file) {
        return Result.success(fileService.addFile(file));
    }

    @PostMapping("/projects/files/pictures")
    public Result createImageFile(@ModelAttribute File file, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception{
        String url = aliyunOSSOperator.upload(multipartFile.getBytes(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.setSource(url);
        return Result.success(fileService.addFile(file));
    }

    @PostMapping("/projects/files/videos")
    public Result createVideoFile(@ModelAttribute File file, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception{
        String url = aliyunOSSOperator.upload(multipartFile.getBytes(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.setSource(url);
        return Result.success(fileService.addFile(file));
    }

    @PutMapping("/projects/{projectId}/files")
    public Result updateFile(@RequestBody File file) {
        fileService.updateFile(file);
        return Result.success();
    }

    @DeleteMapping("/projects/files/{fileId}")
    public Result deleteFile(@PathVariable Integer fileId) {
        log.info("Delete file");
        fileService.deleteFileById(fileId);
        return Result.success();
    }
}
