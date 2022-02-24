package com.unina.natourkt.presentation.post_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentPostDetailsBinding
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: PostDetailsFragmentArgs by navArgs()
    private var user: User? = null

    private val postDetailsViewModel: PostDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        user = mainViewModel.loggedUser
        setupUi()
        setupToolbar()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
    }

    /**
     * Basic settings for UI
     */
    fun setupUi() {
        binding.topAppBar.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }
    }

    /**
     * Options menu
     */
    fun setupToolbar() {
        binding.topAppBar.apply {
            menu.clear()

            if (user!!.id == args.authorId) {
                inflateMenu(R.menu.top_bar_owner_post_menu)
            } else {
                inflateMenu(R.menu.top_bar_viewer_post_menu)
            }

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_post -> {
                        true
                    }
                    R.id.delete_post -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    /**
     * Start to collect [PostDetailsUiState], action based on Success/Loading/Error
     */
    private fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postDetailsViewModel.uiState.collectLatest { uiState ->
                    // When the response arrives
                    if (uiState.post != null) {
                        bindView(uiState.post)
                    }
                    if (uiState.isLoading) {
                        loadingView()
                    }
                }
            }
        }
    }

    /**
     * Bind view to [PostUiState]
     */
    private fun bindView(post: PostUiState) {
        binding.apply {

            progressBar.inVisible()

            authorName.apply {
                text = post.authorUsername
                visible()
            }
            routeName.apply {
                text = post.routeTitle
                visible()
            }

            postDescription.apply {
                text = post.description
                visible()
            }

            authorPhoto.apply {
                GlideApp.with(this@PostDetailsFragment)
                    .load(post.authorPhoto)
                    .error(R.drawable.ic_avatar_svgrepo_com)
                    .into(this)

                visible()
            }

            imageSlider.apply {
                val imageList = post.photos.map { SlideModel(it) }
                setImageList(imageList, ScaleTypes.CENTER_CROP)
                visible()
            }

            chatButton.isVisible = user!!.id != args.authorId
        }
    }

    private fun loadingView() {
        binding.apply {
            progressBar.visible()
            chatButton.inVisible()
            authorName.inVisible()
            routeName.inVisible()
            postDescription.inVisible()
            authorPhoto.inVisible()
            imageSlider.inVisible()
        }
    }
}