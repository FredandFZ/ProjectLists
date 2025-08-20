package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Reply;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetail {
    private Integer id;
    private String title;
    private Integer channelId;
    private Integer authorId;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private List<ReplyDetail> replies;
    private String creatorName;

    public PostDetail(Post post, List<ReplyDetail> replies, String creatorName){
        this.replies = replies;
        this.creatorName = creatorName;
        this.id = post.getId();
        this.title = post.getTitle();
        this.channelId = post.getChannelId();
        this.authorId = post.getAuthorId();
        this.content = post.getContent();
        this.createTime = post.getCreateTime();
        this.updateTime = post.getUpdateTime();
    }
}
