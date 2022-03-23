package com.unina.natourkt.feature_profile.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.ViewPagerAdapter
import com.unina.natourkt.databinding.FragmentProfileBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.feature_profile.profile.compilations.ProfileCompilationsFragment
import com.unina.natourkt.feature_profile.profile.posts.ProfilePostsFragment
import com.unina.natourkt.feature_profile.profile.routes.ProfileRoutesFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Da cambiare in fretta ViewModel!
 */
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun setListeners() {
        binding.settingsChip.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.changePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_profileBottomSheet)
        }
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            setupUserInfo(it.loggedUser)
        }
    }

    private fun setupUserInfo(loggedUser: UserUi?) = with(binding) {
        profilePhoto.loadWithGlide(
            loggedUser?.photo,
            R.drawable.ic_avatar_icon
        )

        textviewUsername.text = loggedUser?.username
    }

    private fun setupViewPager() = with(binding) {

        val fragmentList = arrayListOf<Fragment>(
            ProfilePostsFragment(),
            ProfileCompilationsFragment(),
            ProfileRoutesFragment()
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

