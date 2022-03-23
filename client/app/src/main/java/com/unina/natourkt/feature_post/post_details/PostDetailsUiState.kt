package com.unina.natourkt.feature_post.post_details

import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.PostDetailsUi
import com.unina.natourkt.core.presentation.model.UserUi

data class PostDetailsUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val loggedUser: UserUi? = null,
    val post: PostDetailsUi? = null,
    val retrievedChat: ChatItemUi? = null,
) {

    val menu: Int?
        get() {
            return if (loggedUser != null && post?.authorId != null) {
                if (loggedUser.id == post.authorId) {
                    return R.menu.top_bar_owner_post_menu
                } else {
                    return R.menu.top_bar_viewer_post_menu
                }
            } else {
                null
            }
        }

    val canContactAuthor: Boolean
        get() = loggedUser?.id != post?.authorId
}