package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.common.loadWithGlide
import com.unina.natourkt.databinding.CompilationItemBinding
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState

/**
 * Implementation of PagingDataAdapter for [CompilationItemUiState]
 */
class CompilationAdapter(val listener: OnItemClickListener) :
    PagingDataAdapter<CompilationItemUiState, CompilationAdapter.CompilationViewHolder>(
        CompilationComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CompilationViewHolder(
            CompilationItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CompilationAdapter.CompilationViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CompilationViewHolder(val binding: CompilationItemBinding) :
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

        fun bind(compilation: CompilationItemUiState) = with(binding) {
            // Set basic details
            compilationTitle.text = compilation.title
            compilationDescription.text = compilation.description
            // Load images
            authorPhoto.loadWithGlide(compilation.authorPhoto, R.drawable.ic_avatar_svgrepo_com)
            compilationPhoto.loadWithGlide(compilation.photo)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(compilation: CompilationItemUiState)
    }

    object CompilationComparator : DiffUtil.ItemCallback<CompilationItemUiState>() {
        override fun areItemsTheSame(
            oldItem: CompilationItemUiState,
            newItem: CompilationItemUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CompilationItemUiState,
            newItem: CompilationItemUiState
        ): Boolean {
            return oldItem == newItem
        }
    }
}