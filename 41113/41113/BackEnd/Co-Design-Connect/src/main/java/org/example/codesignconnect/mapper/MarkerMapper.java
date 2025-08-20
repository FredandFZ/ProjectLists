package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.codesignconnect.model.Marker;

import java.util.List;

@Mapper
public interface MarkerMapper {
    List<Marker> findAllByProjectId(Integer projectId);

    void insert(Marker marker);

    void update(Marker marker);

    void delete(Integer id);
}
