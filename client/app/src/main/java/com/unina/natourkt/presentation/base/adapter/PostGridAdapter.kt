package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.databinding.PostGridItemBinding
import com.unina.natourkt.presentation.base.ui_state.PostGridItemUiState

/**
 * Implementation of PagingDataAdapter for [PostGridItemUiState] (posts on profile and route detail screen)
 */
class PostGridAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<PostGridItemUiState, PostGridAdapter.PostGridViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostGridViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostGridItemBinding.inflate(layoutInflater, parent, false)

        return PostGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostGridAdapter.PostGridViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class PostGridViewHolder(val binding: PostGridItemBinding) :
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

        /**
         * Binder function, relies on [Glide] and [SlideModel]
         */
        fun bind(post: PostGridItemUiState) {
            binding.apply {

                // If the user photo is present, then load with Glide
                GlideApp.with(this.root)
                    .load(post.previewPhoto)
                    .fallback(R.drawable.ic_avatar_svgrepo_com)
                    .into(previewPost)

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: PostGridItemUiState)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PostGridItemUiState>() {
        override fun areItemsTheSame(
            oldItem: PostGridItemUiState,
            newItem: PostGridItemUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PostGridItemUiState,
            newItem: PostGridItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}