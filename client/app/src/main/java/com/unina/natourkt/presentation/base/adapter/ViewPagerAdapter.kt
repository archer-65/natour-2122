package com.unina.natourkt.presentation.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment

/**
 * Generic ViewPagerAdapter, accepts three values:
 * - [ArrayList] of [Fragment], so we can avoid hardcoding the number of Fragments;
 * - [FragmentManager] to interact with Fragments
 * - [Lifecycle] related to Fragment
 */
class ViewPagerAdapter(
    val fragmentList: ArrayList<BaseFragment<out ViewBinding, out ViewModel>>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
       return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}