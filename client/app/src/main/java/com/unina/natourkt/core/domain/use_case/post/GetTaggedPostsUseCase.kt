package com.unina.natourkt.core.domain.use_case.post

import com.unina.natourkt.core.domain.repository.PostRepository
import javax.inject.Inject

class GetTaggedPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(routeId: Long) = postRepository.getTaggedPosts(routeId)
}