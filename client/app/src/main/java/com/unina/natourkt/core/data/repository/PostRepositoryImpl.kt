package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.data.paging.PersonalPostPagingSource
import com.unina.natourkt.core.data.paging.PostPagingSource
import com.unina.natourkt.core.data.paging.TaggedPostPagingSource
import com.unina.natourkt.core.data.remote.dto.mapper.PostApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.PostCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.PostApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [PostRepository] contains [Post] related functions for incoming
 * responses from [PostApi] and uses [PostPagingSource] to Paginate with
 * Paging 3 library
 */
class PostRepositoryImpl @Inject constructor(
    private val api: PostApi,
    private val postApiMapper: PostApiMapper,
    private val postCreationApiMapper: PostCreationApiMapper,
) : PostRepository {

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
            pagingSourceFactory = { PostPagingSource(api, postApiMapper) }
        ).flow
    }

    override fun getPersonalPosts(userId: Long): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonalPostPagingSource(api, postApiMapper, userId) }
        ).flow
    }

    override fun getTaggedPosts(routeId: Long): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TaggedPostPagingSource(api, postApiMapper, routeId) }
        ).flow
    }

    override suspend fun getPostDetails(id: Long): DataState<Post> =
        safeApiCall(IO) {
            val postResponse = api.getPostById(id)
            postApiMapper.mapToDomain(postResponse)
        }

    override suspend fun createPost(post: PostCreation): DataState<Unit> =
        safeApiCall(IO) {
            val postRequest = postCreationApiMapper.mapToDto(post)
            api.createRoute(postRequest)
        }
}