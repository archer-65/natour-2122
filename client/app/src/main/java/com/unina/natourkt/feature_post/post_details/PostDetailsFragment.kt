package com.unina.natourkt.feature_post.post_details

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.core.util.*
import com.unina.natourkt.databinding.FragmentPostDetailsBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.PostDetailsUi
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.presentation.util.inVisible
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.visible
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

                if (it.post != null) {
                    bindView(it.post)
                }
                if (it.isLoading) {
                    loadingView()
                }
            }
        }
    }

    private fun setupToolbar(user: UserUi?) = with(binding) {
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
    private fun bindView(post: PostDetailsUi) = with(binding) {

        constraintLayout.visible()

        authorName.text = post.authorUsername
        routeName.text = post.routeTitle
        postDescription.text = post.description
        authorPhoto.loadWithGlide(post.authorPhoto, R.drawable.ic_avatar_svgrepo_com)

        imageSlider.apply {
            val imageList = post.photos.map { SlideModel(it) }
            setImageList(imageList, ScaleTypes.CENTER_CROP)
        }

        progressBar.inVisible()
    }

    private fun loadingView() = with(binding) {
        progressBar.visible()
        constraintLayout.inVisible()
    }
}
