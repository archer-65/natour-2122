package com.unina.springnatour.dto.post;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.post.Post;
import com.unina.springnatour.model.post.PostPhoto;
import org.mapstruct.*;

import java.util.List;

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

    @AfterMapping
    default void mapBidirectional(@MappingTarget Post post){

        List<PostPhoto> photos = post.getPhotos();
        if (photos != null) {
            photos.forEach(photo -> photo.setPost(post));
        }
    }
}
