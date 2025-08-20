package org.example.codesignconnect.service;

import org.example.codesignconnect.dto.FileDetail;
import org.example.codesignconnect.model.File;

import java.util.List;

public interface FileService {
    File getFileById(Integer id);
    List<File> getAllFiles();
    FileDetail addFile(File file);
    void updateFile(File file);
    void deleteFileById(Integer id);
    List<FileDetail> getFilesInFolder(Integer projectId, Short projectStatus, Integer iterationId);
}
