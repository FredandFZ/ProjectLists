package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.ReplyMapper;
import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public int addReply(Reply reply) {
        return replyMapper.insertReply(reply);
    }

    @Override
    public List<Reply> getRepliesByPostId(Integer postId) {
        return replyMapper.getRepliesByPostId(postId);
    }
}
