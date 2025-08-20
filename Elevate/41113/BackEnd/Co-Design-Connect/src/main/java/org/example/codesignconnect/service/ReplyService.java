package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Reply;

import java.util.List;

public interface ReplyService {
    int addReply(Reply reply);
    List<Reply> getRepliesByPostId(Integer postId);
}
