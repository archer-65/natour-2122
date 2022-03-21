package com.unina.natourkt.feature_chat.chat_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.ChatAdapter
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.scrollBehavior
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentChatListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding, ChatListViewModel>(),
    ChatAdapter.OnItemCLickListener {

    private val recyclerAdapter = ChatAdapter(this@ChatListFragment)

    private val viewModel: ChatListViewModel by viewModels()

    override fun getViewBinding() = FragmentChatListBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun setListeners() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                recyclerAdapter.refresh()
            }

            newChatFab.setOnClickListener {
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerChats.apply {
                layoutManager = LinearLayoutManager(this@ChatListFragment.requireContext())
                adapter = initConcatAdapter()
                scrollBehavior(newChatFab)
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
        val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            shimmerContainer.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerChats.isVisible = loadState.source.refresh !is LoadState.Loading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(chatsFlow) {
            recyclerAdapter.submitData(it)
        }
    }

    override fun onItemClick(chat: ChatItemUi) {
        val action = ChatListFragmentDirections.actionNavigationChatListToChatFragment(
            chat,
            viewModel.uiState.value.loggedUser!!.id
        )

        findNavController().navigate(action)
    }
}