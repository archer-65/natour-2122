package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.databinding.UserItemBinding

class UserAdapter(val listener: OnItemCLickListener) :
    PagingDataAdapter<UserUi, UserAdapter.UserViewHolder>(UserComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class UserViewHolder(val binding: UserItemBinding) :
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

        fun bind(user: UserUi) = with(binding) {
            otherUser.text = user.username
            otherUserPhoto.loadWithGlide(user.photo, R.drawable.ic_avatar_icon)
        }
    }

    interface OnItemCLickListener {
        fun onItemClick(user: UserUi)
    }

    object UserComparator : DiffUtil.ItemCallback<UserUi>() {
        override fun areItemsTheSame(oldItem: UserUi, newItem: UserUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserUi, newItem: UserUi): Boolean {
            return oldItem == newItem
        }
    }
}