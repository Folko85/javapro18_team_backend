package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.TagRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;


    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ListResponse getTags(String tag, Integer offset, Integer itemPerPage) {
        List<Tag> tags = tagRepository.findAll();
        int total = tags.size();
        if (tag != null && !tag.isEmpty()) {
            tags = tags.stream().filter(x -> x.getTag().startsWith(tag)).collect(Collectors.toList());
        }
        if (itemPerPage != null && tags.size() > itemPerPage) {
            tags = tags.subList(0, itemPerPage);
        }
        return new ListResponse(); //.setTimestamp(Instant.now()).setError("all right").setOffset(offset).setPerPage(itemPerPage).setTotal(total).setData(tags);
    }

    public DataResponse postTag(Tag tag) {
        Tag savedTag = tagRepository.save(tagRepository.findByTag(tag.getTag()).orElse(tag));
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
