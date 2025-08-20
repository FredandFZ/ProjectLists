package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Marker;
import org.example.codesignconnect.model.Result;

import java.util.List;

public interface MarkerService {
    List<Marker> findAllByProjectId(Integer projectId);

    void insert(Marker marker);

    void update(Marker marker);

    void delete(Integer id);
}
