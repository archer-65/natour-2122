package com.unina.springnatour.controller;

import com.unina.springnatour.dto.post.PostDto;
import com.unina.springnatour.model.post.Post;
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

        if (!postDtoList.isEmpty()) {
            return new ResponseEntity<>(postDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        List<PostDto> postDtoList = postService.getAllPosts(pageNo, pageSize);

        if (!postDtoList.isEmpty()) {
            return new ResponseEntity<>(postDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the posts for a certain user
     *
     * @param userId the identifier of the user (Author)
     * @return List of PostDTO Objects with HTTP Status OK if the list is not empty
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
