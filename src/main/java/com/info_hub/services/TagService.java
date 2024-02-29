package com.info_hub.services;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.tag.TagDTO;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.Tag;
import com.info_hub.repositories.TagRepository;
import com.info_hub.responses.ListResponse;
import com.info_hub.responses.tag.TagResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TagService extends BaseService<Tag> {
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository,
                      ModelMapper modelMapper) {
        super(tagRepository);
        this.modelMapper = modelMapper;
        this.tagRepository = tagRepository;
    }

    public ListResponse<TagResponse> getAllTags(Map<String, String> params) {
        ListResponse<Tag> entityResponse = this.getAll(params);

        // convert entity data to dto data
        List<TagResponse> data = entityResponse.getData().stream()
                .map(entity -> modelMapper.map(entity, TagResponse.class))
                .toList();

        return ListResponse.<TagResponse>builder()
                .data(data)
                .page(entityResponse.getPage())
                .limit(entityResponse.getLimit())
                .totalPage(entityResponse.getTotalPage())
                .totalItems(entityResponse.getTotalItems())
                .build();
    }

    public TagResponse getTagById(Integer id) {
        Optional<Tag> tag = this.getById(id);
        if (tag.isPresent()) {
            return modelMapper.map(tag, TagResponse.class);
        } else {
            throw new BadRequestException("Category not found with id: " + id);
        }
    }

    public ResponseMessage createTag(TagDTO tagDTO) {
        String name = tagDTO.getName();
        String code = tagDTO.getCode();

        // check unique name and code then save
        validateCreateTag(name, code);

        Tag newTag = Tag.builder()
                .name(name)
                .code(code)
                .build();

        tagRepository.save(newTag);
        return ResponseMessage.success();
    }

    public void validateCreateTag(String name, String code) {
        boolean isNameUnique = tagRepository.existsByName(name);
        boolean isCodeUnique = tagRepository.existsByCode(code);

        if (isCodeUnique) {
            throw new BadRequestException("The code of category is exist!");
        }

        if (isNameUnique) {
            throw new BadRequestException("The name of category is exist!");
        }
    }

    public ResponseMessage updateTag(Integer id, TagDTO tagUpdate) {

        Tag existingTag = tagRepository.findById(id).orElseThrow(()
                -> new BadRequestException("Tag not found with id: " + id));
        // if the data don't change -> not update
        validateUpdateTag(existingTag, tagUpdate);

        tagRepository.save(existingTag);
        return ResponseMessage.success();
    }

    private void validateUpdateTag(Tag existTag, TagDTO tagUpdate) {
        String name = tagUpdate.getName();
        String code = tagUpdate.getCode();

        // if the name change
        if (name != null) {
            if (!existTag.getName().equals(name)) {
                boolean isNameExist = tagRepository.existsByName(name);
                if (isNameExist) {
                    throw new BadRequestException("The name of tag is exist!");
                }
                existTag.setName(name);
            }
        }


        // if the code change
        if (code != null) {
            if (!existTag.getCode().equals(code)) {
                boolean isCodeExist = tagRepository.existsByCode(code);

                if (isCodeExist) {
                    throw new BadRequestException("The code of tag is exist!");
                }
                existTag.setCode(code);
            }
        }
    }

    public ResponseMessage deleteTag(int id) {
        Tag existTag = tagRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tag not found with id: " + id));

        tagRepository.delete(existTag);

        return ResponseMessage.success();
    }

}
