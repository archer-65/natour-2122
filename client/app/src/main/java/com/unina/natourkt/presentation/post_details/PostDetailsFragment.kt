package com.unina.natourkt.presentation.post_details

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentPostDetailsBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.UserUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding, PostDetailsViewModel>() {

    private val viewModel: PostDetailsViewModel by viewModels()
    private val args: PostDetailsFragmentArgs by navArgs()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPostDetailsBinding.inflate(layoutInflater)


    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                setupToolbar(it.loggedUser)
                chatButton.isVisible = it.loggedUser?.id != args.authorId
            }

            collectLatestOnLifecycleScope(uiState) {
                if (it.post != null) {
                    bindView(it.post)
                }
                if (it.isLoading) {
                    loadingView()
                }
            }
        }
    }

    fun setupToolbar(user: UserUiState?) = with(binding) {
        topAppBar.apply {
            menu.clear()

            if (user?.id == args.authorId) {
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
    private fun bindView(post: PostUiState) = with(binding) {

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
    }

    private fun loadingView() = with(binding) {
        progressBar.visible()
        chatButton.inVisible()
        authorName.inVisible()
        routeName.inVisible()
        postDescription.inVisible()
        authorPhoto.inVisible()
        imageSlider.inVisible()
    }
}
