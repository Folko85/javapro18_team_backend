package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.tagdto.TagDto;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.PostRepository;
import com.skillbox.socialnetwork.repository.TagRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;


    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public ListResponse<TagDto> getTags(String tag, Integer offset, Integer itemPerPage) {
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Tag> pagebleTagList = tagRepository.findTagsByTextContaining(tag, pageable);
        List<TagDto> result = pagebleTagList.stream().map(x -> new TagDto().setId(x.getId()).setTag(x.getTag())).collect(Collectors.toList());
        ListResponse<TagDto> response = new ListResponse<>();
        response.setPerPage(itemPerPage);
        response.setTimestamp(Instant.now());
        response.setOffset(offset);
        response.setTotal((int) pagebleTagList.getTotalElements());
        response.setData(result);
        return response;
    }

    public DataResponse<TagDto> postTag(TagDto tag) {
        DataResponse<TagDto> response = new DataResponse<>();
        Tag savedTag = tagRepository.save(tagRepository.findByTag(tag.getTag()).orElse(new Tag().setTag(tag.getTag())));
        response.setError("all right");
        response.setTimestamp(Instant.now());
        BeanUtils.copyProperties(savedTag, tag);
        response.setData(tag);
        return response;
    }


    public AccountResponse deleteTag(int id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag is not exist"));
        Set<Post> postsWithTag = postRepository.findPostsByTag(tag.getTag());
        postsWithTag.forEach(p -> {
            p.getTags().remove(tag);
            postRepository.save(p);
        });
        tagRepository.deleteById(id);
        AccountResponse response = new AccountResponse();
        response.setTimestamp(Instant.now());
        response.setError("nothing");
        response.setData(Map.of("message", "ok"));
        return response;
    }
}
