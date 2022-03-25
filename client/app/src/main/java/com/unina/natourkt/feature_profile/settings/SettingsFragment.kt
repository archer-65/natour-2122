package com.unina.natourkt.feature_profile.settings

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.os.BuildCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.setTopMargin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Toolbar>(R.id.toolbarSettings)?.let {
            it.setTopMargin()
            it.setNavigationOnClickListener { findNavController().navigateUp() }
        }

        collectState()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val preference: Preference? = findPreference(getString(R.string.pref_key_night))
        preference?.onPreferenceChangeListener = modeChangeListener

        val logoutOption: Preference? = findPreference(getString(R.string.pref_key_logout))
        logoutOption?.setOnPreferenceClickListener { viewModel.onEvent(SettingsEvent.OnLogout); true }
    }

    private fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            if (it.isOperationCompleted) {
                findNavController().navigate(R.id.action_settingsFragment_to_navigation_auth_flow)
            }
        }
    }

    private val modeChangeListener = object : Preference.OnPreferenceChangeListener {
        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
            newValue as? String
            when (newValue) {
                getString(R.string.pref_night_on) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                getString(R.string.pref_night_off) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    }
                }
            }
            viewModel.onEvent(SettingsEvent.ThemeChange)
            return true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}