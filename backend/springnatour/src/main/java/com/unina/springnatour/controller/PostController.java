package com.unina.springnatour.controller;

import com.unina.springnatour.dto.post.PostDto;
import com.unina.springnatour.model.post.Post;
import com.unina.springnatour.service.PostService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Gets a post
     *
     * @param id the identifier of the post
     * @return PostDTO
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {

        PostDto postDto = postService.getPostById(id);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    /**
     * Gets all the posts
     *
     * @return List of PostDTO
     */
    @GetMapping("/posts/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {

        List<PostDto> postDtoList = postService.getAllPosts();

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
     *
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<PostDto> postDtoList = postService.getAllPosts(pageNo, pageSize);

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
     * Gets all the posts for a certain user
     *
     * @param userId the identifier of the user (Author)
     * @return List of PostDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@RequestParam(name = "user_id") Long userId) {

        List<PostDto> postDtoList = postService.getAllPostsByUserId(userId);

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
     * Gets all the posts for a certain user (paginated)
     *
     * @param userId the identifier of the user (Author)
     * @return List of PostDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/posts/search_page")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<PostDto> postDtoList = postService.getAllPostsByUserId(userId, pageNo, pageSize);

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @GetMapping("/posts/tag")
    public ResponseEntity<List<PostDto>> getAllPostsByRouteId(
            @RequestParam(name = "route_id") Long routeId,
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<PostDto> postDtoList = postService.getAllPostsByRouteId(routeId, pageNo, pageSize);

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
     * Creates a new post
     *
     * @param postDto the PostDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/posts/add")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto) {

        postService.addPost(postDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing post
     *
     * @param id      the identifier of the post
     * @param postDto the PostDTO Object containing the updated post
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/posts/{id}/update")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @RequestBody PostDto postDto) {

        postService.updatePost(id, postDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/posts/{id}/report")
    public ResponseEntity<?> reportPost(@PathVariable Long id) {

        postService.reportPost(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes an existing post
     *
     * @param id the identifier of the post
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/posts/{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {

        postService.deletePost(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
