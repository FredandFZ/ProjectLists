package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.dto.FileDetail;
import org.example.codesignconnect.mapper.FileMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.File;
import org.example.codesignconnect.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public File getFileById(Integer id) {
        return fileMapper.selectFileById(id);
    }

    @Override
    public List<File> getAllFiles() {
        return fileMapper.selectAllFiles();
    }

    @Override
    public FileDetail addFile(File file) {
        fileMapper.insertFile(file);
        return new FileDetail(file, userMapper.getUsernameById(file.getCreatorId()));
    }

    @Override
    public void updateFile(File file) {
        fileMapper.updateFile(file);
    }

    @Override
    public void deleteFileById(Integer id) {
        fileMapper.deleteFileById(id);
    }

    @Override
    public List<FileDetail> getFilesInFolder(Integer projectId, Short projectStatus, Integer iterationId) {
        List<File> fileList = fileMapper.selectFilesInFolder(projectId, projectStatus, iterationId);
        List<FileDetail> fileDetailList = new ArrayList<>();
        for (File file : fileList) {
            fileDetailList.add(new FileDetail(file, userMapper.getUsernameById(file.getCreatorId())));
        }
        return fileDetailList;
    }
}
