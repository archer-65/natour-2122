package com.unina.natourkt.feature_profile.settings

sealed class SettingsEvent {
    object OnLogout: SettingsEvent()
}