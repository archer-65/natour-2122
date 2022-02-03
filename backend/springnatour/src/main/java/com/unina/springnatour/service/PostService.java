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

    /**
     * Gets a post
     * @param id the identifier of the post
     * @return PostDTO Object after mapping from Entity, or throws Exception
     */
    public PostDto getPostById(Long id) {
        return postMapper.toDto(postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id)));
    }

    /**
     * Gets all the posts
     * @return a List of PostDTO Objects after mapping from Entity, or throws Exception
     */
    public List<PostDto> getAllPosts() {
        return postMapper.toDto(postRepository.findAll()
                .stream()
                .toList());
    }

    /**
     * Gets all the posts for a certain user
     * @param userId the identifier of the user
     * @return a List of PostDTO Objects after mapping from Entity, or throws Exception
     */
    public List<PostDto> getAllPostsByUserId(Long userId) {
        return postMapper.toDto(postRepository.findByUser_id(userId)
                .stream()
                .toList());
    }

    /**
     * Adds a post
     * @param postDto PostDTO Object with required fields, mapped to Entity and saved
     */
    public void addPost(PostDto postDto) {
        postRepository.save(postMapper.toEntity(postDto));
    }

    /**
     * Updates a post
     * @param id the identifier of the post
     * @param postDto PostDTO Object, mapped to Entity, or throws Exception
     */
    public void updatePost(Long id, PostDto postDto) {
        postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.save(postMapper.toEntity(postDto));
    }

    /**
     * Deletes a post
     * @param id the identifier of the post
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}