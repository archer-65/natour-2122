package com.unina.springnatour.dto.post;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.post.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper extends BaseMapper<Post, PostDto> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "routeId", source = "route.id")
    @Mapping(target = "photos", source = "photos")
    PostDto toDto(Post post);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "route.id", source = "routeId")
    @Mapping(target = "photos", source = "photos")
    Post toEntity(PostDto postDto);
}
