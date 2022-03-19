package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.databinding.ChatItemBinding

class ChatAdapter(val listener: OnItemCLickListener) :
    PagingDataAdapter<ChatItemUi, ChatAdapter.ChatViewHolder>(ChatComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ChatViewHolder(val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(chat: ChatItemUi) = with(binding) {
            otherUser.text = chat.otherMemberUsername
            otherUserPhoto.loadWithGlide(chat.otherMemberPhoto, R.drawable.ic_avatar_svgrepo_com)
        }
    }

    interface OnItemCLickListener {
        fun onItemClick(chat: ChatItemUi)
    }

    object ChatComparator : DiffUtil.ItemCallback<ChatItemUi>() {
        override fun areItemsTheSame(oldItem: ChatItemUi, newItem: ChatItemUi): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: ChatItemUi, newItem: ChatItemUi): Boolean {
            return oldItem == newItem
        }
    }
}