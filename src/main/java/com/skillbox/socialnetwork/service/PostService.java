package com.skillbox.socialnetwork.service;


import com.skillbox.socialnetwork.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    List<Post> getAll();
    List<Post> getByText(String text, LocalDateTime dateFrom, LocalDateTime dateTo, int offset);
}
