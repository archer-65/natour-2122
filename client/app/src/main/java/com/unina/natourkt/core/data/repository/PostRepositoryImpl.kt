package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.data.paging.PersonalPostSource
import com.unina.natourkt.core.data.paging.PostSource
import com.unina.natourkt.core.data.paging.TaggedPostSource
import com.unina.natourkt.core.data.remote.dto.mapper.PostApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.PostCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.PostApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

    override fun getPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostSource(api, postApiMapper) }
        ).flow
    }

    override fun getPersonalPosts(userId: Long): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonalPostSource(api, postApiMapper, userId) }
        ).flow
    }

    override fun getTaggedPosts(routeId: Long): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TaggedPostSource(api, postApiMapper, routeId) }
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
            api.createPost(postRequest)
        }

    override suspend fun reportPost(postId: Long): DataState<Unit> =
        safeApiCall(IO) {
            api.reportPost(postId)
        }

    override suspend fun deletePost(postId: Long): DataState<Unit> =
        safeApiCall(IO) {
            api.deletePost(postId)
        }
}