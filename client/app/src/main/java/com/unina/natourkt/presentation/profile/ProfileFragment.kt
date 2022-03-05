package com.unina.natourkt.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.common.loadWithGlide
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentProfileBinding
import com.unina.natourkt.presentation.base.adapter.ViewPagerAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.UserUiState
import com.unina.natourkt.presentation.profile.compilations.PersonalCompilationsFragment
import com.unina.natourkt.presentation.profile.posts.PersonalPostsFragment
import com.unina.natourkt.presentation.profile.routes.PersonalRoutesFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Da cambiare in fretta ViewModel!
 */
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            setupUserInfo(it.loggedUser)
        }
    }

    private fun setupUserInfo(loggedUser: UserUiState?) = with(binding) {
        profilePhoto.loadWithGlide(
            loggedUser?.photo,
            R.drawable.ic_avatar_svgrepo_com
        )

        textviewUsername.text = loggedUser?.username
    }

    private fun setupViewPager() = with(binding) {

        val fragmentList = arrayListOf(
            PersonalPostsFragment(),
            PersonalCompilationsFragment(),
            PersonalRoutesFragment()
        )

        val adapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        val portraitIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_portrait_24)
        val favoriteIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
        val locationOnIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24)

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.icon = portraitIcon
                1 -> tab.icon = favoriteIcon
                2 -> tab.icon = locationOnIcon
            }
        }.attach()
    }
}

