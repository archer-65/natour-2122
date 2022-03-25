package com.unina.natourkt.feature_post.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.post.GetPostsUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.PostItemUi
import com.unina.natourkt.core.presentation.model.mapper.PostItemUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [HomeFragment]
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase,
    private val postItemUiMapper: PostItemUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostItemUi>>
    val postsFlow: Flow<PagingData<PostItemUi>>
        get() = _postsFlow

    init {
        getPosts()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ClickPost -> analyticsUseCase.sendEvent(ActionEvents.ClickPost)
        }
    }

    private fun getPosts() {
        viewModelScope.launch {
            _postsFlow = getPostsUseCase()
                .map { pagingData ->
                    pagingData.map { post ->
                        val postUi = postItemUiMapper.mapToUi(post)
                        postUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}