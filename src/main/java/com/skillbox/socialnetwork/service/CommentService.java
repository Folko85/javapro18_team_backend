package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.CommentData;
import com.skillbox.socialnetwork.api.response.CommentWallData;
import com.skillbox.socialnetwork.entity.PostComment;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.ZoneOffset.UTC;

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
        commentData.setTime(postComment.getTime().toInstant(UTC));
        if(postComment.getParent()!=null)
        commentData.setParentId(postComment.getParent().getId());
        commentData.setPostId(postComment.getPost().getId());
        return commentData;
    }

    public static List<CommentWallData> getCommentWallData4Response(Set<PostComment> comments)
    {
        List<CommentWallData> commentDataList = new ArrayList<>();
        comments.forEach(postComment -> {
            CommentWallData commentData = getCommentWallData(postComment);
            commentDataList.add(commentData);
        });
        return commentDataList;
    }

    public static CommentWallData getCommentWallData(PostComment postComment) {
        CommentWallData commentData = new CommentWallData();
        commentData.setCommentText(postComment.getCommentText());
        commentData.setBlocked(postComment.isBlocked());
        commentData.setAuthorId(postComment.getPerson().getId());
        commentData.setId(postComment.getId());
        commentData.setTime(UserServiceImpl.convertLocalDateTime(postComment.getTime()));
        if(postComment.getParent()!=null)
            commentData.setParentId(postComment.getParent().getId());
        commentData.setPostId(postComment.getPost().getId());
        return commentData;
    }
}
