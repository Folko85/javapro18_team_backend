package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.tagdto.TagDto;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.TagRepository;
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
        Tag savedTag = tagRepository.save(tagRepository.findByTag(tag.getTag()).orElse(new Tag().setTag(tag.getTag())));
        return new DataResponse();//.setError("all right").setTimestamp(Instant.now()).setData(savedTag);
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
