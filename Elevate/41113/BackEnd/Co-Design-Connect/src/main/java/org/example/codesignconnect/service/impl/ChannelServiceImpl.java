package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.ChannelMapper;
import org.example.codesignconnect.model.Channel;
import org.example.codesignconnect.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public List<Channel> getChannelsByProjectId(Integer projectId) {
        return channelMapper.getChannelsByProjectId(projectId);
    }

    @Override
    public boolean createChannel(Channel channel) {
        return channelMapper.insertChannel(channel) > 0;
    }

    @Override
    public boolean deleteChannel(Integer id) {
        return channelMapper.deleteChannelById(id) > 0;
    }
}
