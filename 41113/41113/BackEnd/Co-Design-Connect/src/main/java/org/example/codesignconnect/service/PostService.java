package org.example.codesignconnect.service;

import org.example.codesignconnect.dto.PostDetail;
import org.example.codesignconnect.model.Post;

import java.util.List;

public interface PostService {
    int addPost(Post post);
    List<PostDetail> getPostsByChannelId(Integer channelId);
}
