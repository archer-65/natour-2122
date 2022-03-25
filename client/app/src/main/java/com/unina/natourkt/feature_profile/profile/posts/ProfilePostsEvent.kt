package com.unina.natourkt.feature_profile.profile.posts

sealed class ProfilePostsEvent {
    object ClickPost: ProfilePostsEvent()
}