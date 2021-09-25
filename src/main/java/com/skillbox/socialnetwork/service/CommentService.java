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
    public static List<CommentData> getCommentData4Response(Set<PostComment> comments)
    {
        List<CommentData> commentDataList = new ArrayList<>();
        comments.forEach(postComment -> {
            CommentData commentData = getCommentData(postComment);
            commentDataList.add(commentData);
        });
        return commentDataList;
    }

    public static CommentData getCommentData(PostComment postComment) {
        CommentData commentData = new CommentData();
        commentData.setCommentText(postComment.getCommentText());
        commentData.setBlocked(postComment.isBlocked());
        commentData.setAuthorId(postComment.getPerson().getId());
        commentData.setId(postComment.getId());
        commentData.setTime(postComment.getTime().toEpochSecond(ZoneOffset.UTC));
        if(postComment.getParent()!=null)
        commentData.setParentId(postComment.getParent().getId());
        commentData.setPostId(postComment.getPost().getId());
        return commentData;
    }
}
