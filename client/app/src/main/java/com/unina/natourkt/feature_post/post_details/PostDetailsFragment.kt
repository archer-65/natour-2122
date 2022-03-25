package com.unina.natourkt.feature_post.post_details

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.core.util.*
import com.unina.natourkt.databinding.FragmentPostDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding, PostDetailsViewModel>() {

    private val viewModel: PostDetailsViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPostDetailsBinding.inflate(layoutInflater)

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun setListeners() {
        binding.chatButton.setOnClickListener {
            viewModel.onEvent(PostDetailsEvent.ShowChat)
        }
    }

    override fun collectState() {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                it.menu?.let { setupToolbar(it) }
                bindView(it)

                if (it.retrievedChat != null) {
                    val action =
                        PostDetailsFragmentDirections.actionNavigationPostDetailsToChatFragment(
                            it.retrievedChat,
                            it.loggedUser!!.id
                        )
                    viewModel.onEvent(PostDetailsEvent.ResetChat)
                    findNavController().navigate(action)
                }
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
                onMenuClick(it.itemId)
            }
        }
    }

    private fun onMenuClick(item: Int): Boolean {
        when (item) {
            R.id.report_post -> {
                val action =
                    PostDetailsFragmentDirections.actionNavigationPostDetailsToReportPostDialog(
                        viewModel.uiState.value.post!!.id
                    )
                findNavController().navigate(action)
            }
            R.id.delete_post -> {
                val action =
                    PostDetailsFragmentDirections.actionNavigationPostDetailsToDeletePostDialog(
                        viewModel.uiState.value.post!!.id
                    )
                findNavController().navigate(action)
            }
        }
        return true
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

                authorPhoto.loadWithGlide(it.authorPhoto, R.drawable.ic_avatar_icon)

                imageSlider.load(it.photos)
            }

            progressBar.isVisible = isLoading
            constraintLayout.isVisible = !isLoading && !isError
            chatButton.isVisible = canContactAuthor
        }
    }
}
