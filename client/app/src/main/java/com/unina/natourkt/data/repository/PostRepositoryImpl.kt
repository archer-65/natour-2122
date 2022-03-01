package com.unina.natourkt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.data.remote.dto.post.toPost
import com.unina.natourkt.data.paging.PersonalPostPagingSource
import com.unina.natourkt.data.paging.PostPagingSource
import com.unina.natourkt.data.remote.retrofit.PostApi
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [PostRepository] contains [Post] related functions for incoming
 * responses from [PostApi] and uses [PostPagingSource] to Paginate with
 * Paging 3 library
 */
class PostRepositoryImpl @Inject constructor(
    private val api: PostApi
): PostRepository {

    /**
     * Page size for network requests for this class
     * NOTE: The first page defaults to pageSize * 3!
     */
    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }

    /**
     * This functions return paginated data for [Post] as a flow
     */
    override fun getPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPagingSource(api) }
        ).flow
    }

    override fun getPersonalPosts(userId: Long): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonalPostPagingSource(api, userId) }
        ).flow
    }

    override suspend fun getPostDetails(id: Long): Post {
        return api.getPostById(id).toPost()
    }
}