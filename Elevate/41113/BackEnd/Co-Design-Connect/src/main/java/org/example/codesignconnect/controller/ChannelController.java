package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Channel;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping("/projects/{projectId}/channel")
    public Result getChannelsByProject(@PathVariable Integer projectId) {
        List<Channel> channels = channelService.getChannelsByProjectId(projectId);
        return Result.success(channels);
    }

    @PostMapping("/projects/{projectId}/channel")
    public Result createChannel(Channel channel) {
        boolean created = channelService.createChannel(channel);
        return created ? Result.success() : Result.error("Failed to create channel");
    }

    @DeleteMapping("/{id}")
    public Result deleteChannel(@PathVariable Integer id) {
        boolean deleted = channelService.deleteChannel(id);
        return deleted ? Result.success() : Result.error("Failed to delete channel");
    }
}
