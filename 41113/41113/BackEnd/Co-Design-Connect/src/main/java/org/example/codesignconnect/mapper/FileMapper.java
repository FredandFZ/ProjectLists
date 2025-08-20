package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.codesignconnect.model.File;

import java.util.List;

@Mapper
public interface FileMapper {
    File selectFileById(Integer id);
    List<File> selectAllFiles();
    int insertFile(File file);
    int updateFile(File file);
    int deleteFileById(Integer id);
    List<File> selectFilesInFolder(Integer projectId, Short projectStatus, Integer iterationId);
}
