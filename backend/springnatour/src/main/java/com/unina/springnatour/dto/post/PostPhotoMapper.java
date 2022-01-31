package com.unina.springnatour.dto.post;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.post.PostPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostPhotoMapper extends BaseMapper<PostPhoto, PostPhotoDto> {

    @Override
    PostPhotoDto toDto(PostPhoto postPhoto);

    @Override
    PostPhoto toEntity(PostPhotoDto postPhotoDto);

}
