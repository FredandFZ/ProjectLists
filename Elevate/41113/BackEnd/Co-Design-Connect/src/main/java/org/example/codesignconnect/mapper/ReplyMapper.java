package org.example.codesignconnect.mapper;

import org.example.codesignconnect.model.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyMapper {
    int insertReply(Reply reply);
    List<Reply> getRepliesByPostId(Integer postId);
}
