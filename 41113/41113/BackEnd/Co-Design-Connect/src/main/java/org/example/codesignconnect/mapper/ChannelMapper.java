package org.example.codesignconnect.mapper;

import org.example.codesignconnect.model.Channel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChannelMapper {
    List<Channel> getChannelsByProjectId(Integer projectId);
    int insertChannel(Channel channel);
    int deleteChannelById(Integer id);
}
