package com.unina.natourkt.feature_chat.chat

import android.widget.Toast
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

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        layoutGchatChatbox.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        buttonGchatSend.setOnClickListener {
            viewModel.onEvent(ChatEvent.SendMessage)
        }

        scrollBottomFab.setOnClickListener {
            recyclerChat.scrollToPosition(0)
            viewModel.onEvent(ChatEvent.ReadAll)
        }

        recyclerChat.scrollChat(binding.scrollBottomFab)
    }

    override fun setTextChangedListeners() {
        binding.editGchatMessage.doAfterTextChanged {
            viewModel.onEvent(ChatEvent.UpdateMessage(it.toString()))
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
        val badge = setBadge()
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

                if (it.shouldResetText) {
                    binding.editGchatMessage.setText("")
                    hideKeyboard()
                }
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEffect.ShowToast -> {
                        Toast.makeText(
                            requireContext(),
                            event.uiText.asString(requireContext()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setBadge(): BadgeDrawable {
        val badge = BadgeDrawable.create(requireContext())
        badge.badgeGravity = BadgeDrawable.TOP_START
        badge.verticalOffset = 40
        badge.horizontalOffset = 40

        return badge
    }

    private fun bindChat(chatInfo: ChatItemUi) = with(binding) {
        toolbarUsername.text = chatInfo.otherMemberUsername
        toolbarAvatar.loadWithGlide(chatInfo.otherMemberPhoto, R.drawable.ic_avatar_icon)
    }
}