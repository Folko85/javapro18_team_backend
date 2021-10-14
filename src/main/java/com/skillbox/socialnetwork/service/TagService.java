package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.tagdto.TagDto;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.TagRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;


    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ListResponse getTags(String tag, Integer offset, Integer itemPerPage) {
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Tag> pagebleTagList = tagRepository.findTagsByTextContaining(tag, pageable);
        List<Dto> result = pagebleTagList.stream().map(x -> new TagDto().setId(x.getId()).setTag(x.getTag())).collect(Collectors.toList());
        ListResponse response = new ListResponse();
        response.setPerPage(itemPerPage);
        response.setTimestamp(Instant.now());
        response.setOffset(offset);
        response.setTotal((int) pagebleTagList.getTotalElements());
        response.setData(result);
        return response;
    }

    public DataResponse postTag(TagDto tag) {
        DataResponse response = new DataResponse();
        Tag savedTag = tagRepository.save(tagRepository.findByTag(tag.getTag()).orElse(new Tag().setTag(tag.getTag())));
        response.setError("all right");
        response.setTimestamp(Instant.now());
        BeanUtils.copyProperties(savedTag, tag);
        response.setData(tag);
        return response;
    }


    public AccountResponse deleteTag(int id) {
        if (tagRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Tag is not exist");
        } else tagRepository.deleteById(id);
        return new AccountResponse();//.setError("all right").setData(new HashMap<>() {{
//            put("message", "ok");
//        }}).setTimestamp(Instant.now());
    }
}
