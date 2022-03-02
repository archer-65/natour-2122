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
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentHomeBinding
import com.unina.natourkt.databinding.FragmentPostDetailsBinding
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding, PostDetailsViewModel>() {

    private val viewModel: PostDetailsViewModel by viewModels()
    private val args: PostDetailsFragmentArgs by navArgs()

    private var user: User? = null

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPostDetailsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = mainViewModel.loggedUser
        setupToolbar()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

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

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            // When the response arrives
            if (it.post != null) {
                bindView(it.post)
            }
            if (it.isLoading) {
                loadingView()
            }
        }
    }
}