package com.unina.springnatour.dto.post;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.route.RouteTitleDto;
import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.post.Post;
import com.unina.springnatour.model.post.PostPhoto;
import com.unina.springnatour.model.route.Route;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper extends BaseMapper<Post, PostDto> {

    @Override
    @Mappings({
            @Mapping(target = "postId", source = "id"),
            @Mapping(target = "postDescription", source = "description"),
            @Mapping(target = "isPostReported", source = "reported"),
            @Mapping(target = "postCreationDate", source = "creationDate"),
            @Mapping(target = "postAuthor", source = "user"),
            @Mapping(target = "postPhotos", source = "photos"),
            @Mapping(target = "taggedRoute", source = "route"),
    })
    PostDto toDto(Post post);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "postId"),
            @Mapping(target = "description", source = "postDescription"),
            @Mapping(target = "reported", source = "isPostReported"),
            @Mapping(target = "creationDate", source = "postCreationDate"),
            @Mapping(target = "user", source = "postAuthor"),
            @Mapping(target = "photos", source = "postPhotos"),
            @Mapping(target = "route", source = "taggedRoute"),
    })
    Post toEntity(PostDto postDto);

    @AfterMapping
    default void mapBidirectional(@MappingTarget Post post) {

        List<PostPhoto> photos = post.getPhotos();
        if (photos != null) {
            photos.forEach(photo -> photo.setPost(post));
        }
    }

    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "profilePhoto", source = "photo"),
    })
    UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "photo", source = "profilePhoto"),
    })
    User toEntity(UserDto userDto);

    @Mappings({
            @Mapping(target = "routeId", source = "id"),
            @Mapping(target = "routeTitle", source = "title")
    })
    RouteTitleDto toDto(Route route);

    @Mappings({
            @Mapping(target = "id", source = "routeId"),
            @Mapping(target = "title", source = "routeTitle")
    })
    Route toEntity(RouteTitleDto routeTitleDto);
}
