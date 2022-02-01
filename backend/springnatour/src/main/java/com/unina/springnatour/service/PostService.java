package com.unina.springnatour.service;

import com.unina.springnatour.dto.post.PostDto;
import com.unina.springnatour.dto.post.PostMapper;
import com.unina.springnatour.exception.PostNotFoundException;
import com.unina.springnatour.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    public PostDto getPostById(Long id) {
        return postMapper.toDto(postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id)));
    }

    public List<PostDto> getAllPosts() {
        return postMapper.toDto(postRepository.findAll()
                .stream()
                .toList());
    }

    public List<PostDto> getAllPostsByUserId(Long userId) {
        return postMapper.toDto(postRepository.findByUser_id(userId)
                .stream()
                .toList());
    }

    public void addPost(PostDto post) {
        postRepository.save(postMapper.toEntity(post));
    }

    public void updatePost(Long id, PostDto postDto) {
        postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.save(postMapper.toEntity(postDto));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}