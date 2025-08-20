package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Channel;
import java.util.List;

public interface ChannelService {
    List<Channel> getChannelsByProjectId(Integer projectId);
    boolean createChannel(Channel channel);
    boolean deleteChannel(Integer id);
}