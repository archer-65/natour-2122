package com.unina.natourkt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.data.remote.PostRetrofitDataSource
import com.unina.natourkt.data.remote.paging.PostPagingSource
import com.unina.natourkt.domain.model.post.Post
import com.unina.natourkt.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val retrofitDataSource: PostRetrofitDataSource
): PostRepository {

    override fun getPosts(): Flow<PagingData<Post>> {
        //return retrofitDataSource.getPosts(pageNo).map { post -> post.toPost()  }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPagingSource(retrofitDataSource) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}