package com.unina.natourkt.domain.use_case.post

import com.unina.natourkt.domain.repository.PostRepository
import javax.inject.Inject

class GetTaggedPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(routeId: Long) = postRepository.getTaggedPosts(routeId)
}