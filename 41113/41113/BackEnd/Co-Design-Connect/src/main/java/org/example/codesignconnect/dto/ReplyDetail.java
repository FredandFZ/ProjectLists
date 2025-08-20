package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Reply;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDetail {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String senderName;

    public ReplyDetail(Reply reply, String senderName){
        this.id = reply.getId();
        this.postId = reply.getPostId();
        this.authorId = reply.getAuthorId();
        this.content = reply.getContent();
        this.createTime = reply.getCreateTime();
        this.updateTime = reply.getUpdateTime();
        this.senderName = senderName;
    }
}
