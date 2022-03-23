package com.unina.natourkt.feature_chat.chat_search

import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.UserAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentChatSearchBinding
import com.unina.natourkt.feature_route.route_details.RouteDetailsFragmentDirections
import com.unina.natourkt.feature_route.route_search.RouteSearchEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatSearchFragment : BaseFragment<FragmentChatSearchBinding, ChatSearchViewModel>(),
    UserAdapter.OnItemCLickListener {

    private val recyclerAdapter = UserAdapter(this@ChatSearchFragment)

    private val viewModel: ChatSearchViewModel by viewModels()

    override fun getViewBinding() = FragmentChatSearchBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
    }

    override fun setListeners() = with(binding) {

        topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_chatSearchFragment_to_navigation_chat_list)
        }

        binding.search.onActionViewExpanded()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.onEvent(ChatSearchEvent.EnteredQuery(it))
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun initRecycler() {
        with(binding) {
            recyclerUsers.apply {
                layoutManager = LinearLayoutManager(this@ChatSearchFragment.requireContext())
                adapter = initConcatAdapter()
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
        val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            recyclerUsers.isVisible = loadState.source.refresh is LoadState.NotLoading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(usersResult) {
            recyclerAdapter.submitData(it)
        }

        collectLatestOnLifecycleScope(uiState) {
            binding.progressBar.isVisible = it.isLoading

            if (it.retrievedChat != null) {
                val action = ChatSearchFragmentDirections.actionChatSearchFragmentToChatFragment(
                    it.retrievedChat,
                    loggedUserId
                )
                viewModel.resetChat()
                findNavController().navigate(action)
            }
        }
    }

    override fun onItemClick(user: UserUi) {
        viewModel.getChat(user)
    }
}