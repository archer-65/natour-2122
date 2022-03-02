package com.unina.natourkt.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.common.loadWithGlide
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentProfileBinding
import com.unina.natourkt.presentation.base.adapter.ViewPagerAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
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

//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPage()
        setupUserInfo()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()

        //this@ProfileFragment.tabLayout = binding.tabLayout
        //this@ProfileFragment.viewPager = binding.viewPager
    }

    private fun setupViewPage() = with(binding) {

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
        //viewPager.isSaveEnabled = false
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.icon = portraitIcon
                1 -> tab.icon = favoriteIcon
                2 -> tab.icon = locationOnIcon
            }
        }.attach()
    }

    fun setupUserInfo() = with(binding) {
        profilePhoto.loadWithGlide(
            mainViewModel.loggedUser?.photo,
            R.drawable.ic_avatar_svgrepo_com
        )

        textviewUsername.text = mainViewModel.loggedUser?.username
    }
}

