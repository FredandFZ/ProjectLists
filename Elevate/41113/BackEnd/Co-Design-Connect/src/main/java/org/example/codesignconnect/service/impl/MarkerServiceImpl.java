package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.MarkerMapper;
import org.example.codesignconnect.model.Marker;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.MarkerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MarkerServiceImpl implements MarkerService {
    @Autowired
    private MarkerMapper markerMapper;

    @Override
    public List<Marker> findAllByProjectId(Integer projectId) {
        return markerMapper.findAllByProjectId(projectId);
    }

    @Override
    public void insert(Marker marker) {
        markerMapper.insert(marker);
    }

    @Override
    public void update(Marker marker) {
        markerMapper.update(marker);
    }

    @Override
    public void delete(Integer id) {
        markerMapper.delete(id);
    }
}
