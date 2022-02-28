package com.unina.natourkt.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.databinding.FragmentProfileBinding
import com.unina.natourkt.presentation.base.adapter.ViewPagerAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.profile.compilations.PersonalCompilationsFragment
import com.unina.natourkt.presentation.profile.posts.PersonalPostsFragment
import com.unina.natourkt.presentation.profile.routes.PersonalRoutesFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUi()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPage()
        setupUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        binding.topAppBar.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
    }

    private fun setupViewPage() {

        val fragmentList = arrayListOf(
            PersonalPostsFragment(),
            PersonalCompilationsFragment(),
            PersonalRoutesFragment()
        )

        val portraitIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_portrait_24)
        val favoriteIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
        val locationOnIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24)

        val adapter = ViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            //requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = portraitIcon
                }
                1 -> {
                    tab.icon = favoriteIcon
                }
                2 -> {
                    tab.icon = locationOnIcon
                }
            }
        }.attach()
    }

    fun setupUserInfo() {
        GlideApp.with(this)
            .load(mainViewModel.loggedUser?.photo)
            .error(R.drawable.ic_avatar_svgrepo_com)
            .into(binding.profilePhoto)

        binding.textviewUsername.text = mainViewModel.loggedUser?.username
    }
}

