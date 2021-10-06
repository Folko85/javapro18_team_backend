package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.tagdto.DeleteTagResponse;
import com.skillbox.socialnetwork.api.response.tagdto.ManyTagsResponse;
import com.skillbox.socialnetwork.api.response.tagdto.OneTagResponse;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.TagRepository;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TagService {

    private final TagRepository tagRepository;


    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ManyTagsResponse getTags(String tag, int offset, int itemPerPage) {
        List<Tag> tags = tagRepository.findAll();
        int total = tags.size();
        if (tag != null && !tag.isEmpty()) {
            tags = tags.stream().filter(x -> x.getTag().startsWith(tag)).collect(Collectors.toList());
        }
        if (tags.size() > itemPerPage) {
            tags = tags.subList(0, itemPerPage);
        }
        return new ManyTagsResponse().setTimestamp(Instant.now()).setError("all right").setOffset(offset).setPerPage(itemPerPage).setTotal(total).setData(tags);
    }

    public OneTagResponse postTag(Tag tag) {
        Tag savedTag = tagRepository.save(tagRepository.findByTag(tag.getTag()).orElse(tag));
        return new OneTagResponse().setError("all right").setTimestamp(Instant.now()).setData(savedTag);
    }


    public DeleteTagResponse deleteTag(int id) {
        if (tagRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Tag is not exist");
        } else tagRepository.deleteById(id);
        return new DeleteTagResponse().setError("all right").setData(new HashMap<>() {{
            put("message", "ok");
        }}).setTimestamp(Instant.now());
    }
}
