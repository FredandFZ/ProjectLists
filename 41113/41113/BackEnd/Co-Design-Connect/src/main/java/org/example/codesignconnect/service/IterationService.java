package org.example.codesignconnect.service;

import org.example.codesignconnect.dto.FolderResponse;
import org.example.codesignconnect.dto.IterationDetail;
import org.example.codesignconnect.model.Iteration;

import java.util.List;

public interface IterationService {
    int createIteration(Iteration iteration);
    int updateIteration(Iteration iteration);
    int deleteIteration(Integer id);
    List<IterationDetail> getIterations(Integer id, Short status);
    List<Iteration> getIterationsByProjectId(Integer projectId);
    List<FolderResponse> getFolders(Integer projectId);
}