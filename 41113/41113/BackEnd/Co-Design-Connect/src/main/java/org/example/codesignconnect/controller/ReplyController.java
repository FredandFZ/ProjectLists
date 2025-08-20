package org.example.codesignconnect.controller;

import org.example.codesignconnect.dto.ReplyDetail;
import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.service.ReplyService;
import org.example.codesignconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserService userService;

    @PostMapping("/projects/{projectId}/channel/reply")
    public Result createReply(Reply reply) {
        int rows = replyService.addReply(reply);
        String name = userService.getUsernameById(reply.getAuthorId());
        return rows > 0 ? Result.success(new ReplyDetail(reply, name)) : Result.error("Failed to create reply");
    }

    @GetMapping("/post/{postId}")
    public Result getRepliesByPostId(@PathVariable Integer postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return Result.success(replies);
    }
}
