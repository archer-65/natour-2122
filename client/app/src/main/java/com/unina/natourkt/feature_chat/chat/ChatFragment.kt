package com.unina.natourkt.feature_chat.chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.MessageAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.util.*
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
        setTextChangedListeners()
        initRecycler()
    }

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
        binding.layoutGchatChatbox.setBottomMargin()
    }

    override fun setListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonGchatSend.setOnClickListener {
            viewModel.sender()
        }

        binding.scrollBottomFab.setOnClickListener {
            binding.recyclerChat.scrollToPosition(0)
            viewModel.setReadMessages()
        }

        binding.recyclerChat.scrollChat(binding.scrollBottomFab)
    }

    override fun setTextChangedListeners() {
        binding.editGchatMessage.doAfterTextChanged {
            viewModel.messageUpdate(it.toString())
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

    @com.google.android.material.badge.ExperimentalBadgeUtils
    override fun collectState() = with(binding) {
        val badge = BadgeDrawable.create(requireContext())
        badge.verticalOffset = 40
        badge.horizontalOffset = 40


        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                bindChat(it.chatInfo)
                recyclerAdapter.submitList(it.messages)
                progressBar.isVisible = it.isLoading
                buttonGchatSend.isEnabled = it.messageState.isNotBlank()

                badge.number = it.newMessagesNumber
                if (it.shouldResetBadge) {
                    BadgeUtils.detachBadgeDrawable(badge, binding.scrollBottomFab)
                } else {
                    BadgeUtils.attachBadgeDrawable(badge, binding.scrollBottomFab)

                }
            }
        }
    }

    private fun bindChat(chatInfo: ChatItemUi) = with(binding) {
        toolbarUsername.text = chatInfo.otherMemberUsername
        toolbarAvatar.loadWithGlide(chatInfo.otherMemberPhoto, R.drawable.ic_avatar_icon)
    }
}