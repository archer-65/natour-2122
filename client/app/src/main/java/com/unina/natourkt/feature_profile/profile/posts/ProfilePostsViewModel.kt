package com.unina.natourkt.feature_profile.profile.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.post.GetPersonalPostsUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.PostGridItemUi
import com.unina.natourkt.core.presentation.model.mapper.PostGridItemUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [ProfilePostsFragment]
 */
@HiltViewModel
class ProfilePostsViewModel @Inject constructor(
    private val getPersonalPostsUseCase: GetPersonalPostsUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase,
    private val postGridItemUiMapper: PostGridItemUiMapper
) : ViewModel() {

    /**
     * [ProfilePostsUiState] with a set of RouteItemUiState
     */
    // private val _uiState = MutableStateFlow(PersonalPostsUiState())
    // val uiState: StateFlow<PersonalPostsUiState> = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostGridItemUi>>
    val postsFlow: Flow<PagingData<PostGridItemUi>>
        get() = _postsFlow

    init {
        getPersonalPosts()
    }

    fun onEvent(event: ProfilePostsEvent) {
        when (event) {
            ProfilePostsEvent.ClickPost -> analyticsUseCase.sendEvent(ActionEvents.ClickPost)
        }
    }

    private fun getPersonalPosts() {
        viewModelScope.launch {
            _postsFlow = getPersonalPostsUseCase()
                .map { pagingData ->
                    pagingData.map { post ->
                        val postUi = postGridItemUiMapper.mapToUi(post)
                        postUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}