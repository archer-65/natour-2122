package com.unina.springnatour.controller;

import com.unina.springnatour.dto.post.PostDto;
import com.unina.springnatour.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Get a post
     * @param id the identifier of the post
     * @return PostDTO
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {

        PostDto postDto = postService.getPostById(id);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    /**
     * Get all the posts
     * @return List of PostDTO
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {

        List<PostDto> postDtoList = postService.getAllPosts();

        if (!postDtoList.isEmpty()) {
            return new ResponseEntity<>(postDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all the posts for a certain User
     * @param userId the identifier of the User (Author)
     * @return List of PostDTO
     */
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@RequestParam Long userId) {

        List<PostDto> postDtoList = postService.getAllPostsByUserId(userId);

        if (!postDtoList.isEmpty()) {
            return new ResponseEntity<>(postDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new post
     * @param postDto the PostDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/posts/add")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto) {

        postService.addPost(postDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Update existing post
     * @param id the identifier of the post
     * @param postDto the PostDTO Obejct containing updated post
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/posts/{id}/update")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @RequestBody PostDto postDto) {

        postService.updatePost(id, postDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete existing post
     * @param id the identifier of the post
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/posts/{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {

        postService.deletePost(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
