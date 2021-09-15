package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getByText(String text, LocalDateTime dateFrom, LocalDateTime dateTo, int offset) {
        List<Post> list = postRepository.findAll();

        if (text != null) {
            list = list.stream()
                    .filter(post -> post.getPostText().contains(text))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException();
        }

        if (dateFrom != null) {
            list = list.stream().
                    filter(post -> post.getDatetime().isAfter(dateFrom))
                    .collect(Collectors.toList());
        }
        if (dateTo != null) {
            list = list.stream()
                    .filter(post -> post.getDatetime().isBefore(dateTo))
                    .collect(Collectors.toList());
        }
        if (offset != 0) {
            list = list.stream()
                    .skip(offset)
                    .collect(Collectors.toList());
        }
        return list;
    }


}
