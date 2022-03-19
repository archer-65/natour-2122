package com.unina.natourkt.feature_chat.chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.MessageAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {

    private val recyclerAdapter = MessageAdapter()

    private val viewModel: ChatViewModel by viewModels()

    override fun getViewBinding() = FragmentChatBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
        binding.layoutGchatChatbox.setBottomMargin()
    }

    override fun setListeners() {
        binding.buttonGchatSend.setOnClickListener {
            viewModel.sender()
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerChat.apply {
                val layout = LinearLayoutManager(
                    this@ChatFragment.requireContext(),
                    LinearLayoutManager.VERTICAL,
                    true,
                )
                layout.stackFromEnd = false
                layoutManager = layout

                adapter = recyclerAdapter
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                bindChat(it.chatInfo)
                recyclerAdapter.submitList(it.messages)
                progressBar.isVisible = it.isLoading
            }
        }
    }

    private fun bindChat(chatInfo: ChatItemUi) = with(binding) {
        toolbarUsername.text = chatInfo.otherMemberUsername
        toolbarAvatar.loadWithGlide(chatInfo.otherMemberPhoto, R.drawable.ic_avatar_svgrepo_com)
    }
}