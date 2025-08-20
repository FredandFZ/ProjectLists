package org.example.codesignconnect.mapper;

import org.example.codesignconnect.model.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    int insertPost(Post post);
    List<Post> getPostsByChannelId(Integer channelId);
}
