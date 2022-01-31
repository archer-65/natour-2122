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

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {

        PostDto postDto = postService.getPostById(id);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();

        if (!posts.isEmpty()) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<?> addPost(@RequestBody PostDto post) {

        postService.addPost(post);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @RequestBody PostDto postDto) {

        postService.updatePost(id, postDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
