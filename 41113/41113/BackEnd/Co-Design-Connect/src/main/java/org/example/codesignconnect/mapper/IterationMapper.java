package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.codesignconnect.model.Iteration;

import java.util.List;

@Mapper
public interface IterationMapper {
    int insertIteration(Iteration iteration);
    int updateIteration(Iteration iteration);
    int deleteIteration(@Param("id") Integer id);
    List<Iteration> getIterations(@Param("projectId") Integer projectId, Short status);
    List<Iteration> getIterationsByProjectId(@Param("projectId") Integer projectId);
    Short getProjectStatusById(Integer id);
    int getIteratedTimeById(Integer id);
}