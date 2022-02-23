package com.unina.natourkt.presentation.post_details

import android.provider.ContactsContract
import com.unina.natourkt.common.DataState

data class PostDetailsUiState(
   val isLoading: Boolean = false,
   val error: DataState.CustomMessages? = null,
   val  post: PostUiState? = null
)

data class PostUiState(
    val id: Long,
    val description: String,
    val photos: List<String>,
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String?,
    val routeId: Long,
    val routeTitle: String
)