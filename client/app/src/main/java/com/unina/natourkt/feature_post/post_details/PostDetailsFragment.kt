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
import com.unina.natourkt.core.presentation.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding, PostDetailsViewModel>() {

    private val viewModel: PostDetailsViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPostDetailsBinding.inflate(layoutInflater)

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun collectState() {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                it.menu?.let { setupToolbar(it) }
                bindView(it)
            }
        }
    }

    private fun setupToolbar(menu: Int) = with(binding) {
        topAppBar.apply {
            this.menu.clear()
            inflateMenu(menu)

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
    private fun bindView(uiState: PostDetailsUiState) = with(binding) {
        uiState.apply {
            post?.let {
                authorName.text = it.authorUsername
                routeName.text = it.routeTitle
                postDescription.text = it.description

                authorPhoto.loadWithGlide(it.authorPhoto, R.drawable.ic_avatar_svgrepo_com)

                imageSlider.load(it.photos)
            }

            progressBar.isVisible = isLoading
            constraintLayout.isVisible = !isLoading
            chatButton.isVisible = canContactAuthor
        }
    }
}