package com.unina.natourkt.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.databinding.FragmentHomeBinding
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.model.post.Post
import com.unina.natourkt.domain.model.post.PostPhoto
import com.unina.natourkt.presentation.adapter.PostAdapter
import com.unina.natourkt.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.topAppBar.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        // TEST
//        val postPhotos = mutableListOf(
//            PostPhoto(1, "https://www.chiccheinformatiche.com/wp-content/uploads/2016/07/android.jpg"),
//            PostPhoto(2, "https://cdn.mos.cms.futurecdn.net/5NyzBxijspGUiFyCiz9F4-1200-80.jpg")
//        )
//
//        val user = User(2, "Marietto")
//
//        val postList = mutableListOf(
//            Post(1, "Try description this time" , false, postPhotos, user)
//        )
//
//        val adapter = PostAdapter(postList)
//        binding.recyclerHome.adapter = adapter
//        binding.recyclerHome.layoutManager = LinearLayoutManager(this.requireContext())
        // FINE TEST

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}