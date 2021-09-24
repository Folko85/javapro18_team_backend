package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.CommentData;
import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.stereotype.Service;


import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class CommentService {
    static List<CommentData> getCommentData(Set<PostComment> comments)
    {
        List<CommentData> commentDataList = new ArrayList<>();
        comments.forEach(postComment -> {
            CommentData commentData = new CommentData();
            commentData.setCommentText(postComment.getCommentText());
            commentData.setBlocked(postComment.isBlocked());
            commentData.setAuthorId(postComment.getPerson().getId());
            commentData.setId(postComment.getId());
            commentData.setTime(postComment.getTime().toEpochSecond(ZoneOffset.UTC));
            if(postComment.getParent()!=null)
            commentData.setParentId(postComment.getParent().getId());
            commentData.setPostId(postComment.getPost().getId());
            commentDataList.add(commentData);
        });
        return commentDataList;
    }
}
