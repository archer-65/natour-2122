package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.CompilationItemBinding
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide

/**
 * Implementation of PagingDataAdapter for [CompilationItemUi]
 */
class CompilationAdapter(val listener: OnItemClickListener) :
    PagingDataAdapter<CompilationItemUi, CompilationAdapter.CompilationViewHolder>(
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

        fun bind(compilation: CompilationItemUi) = with(binding) {
            // Set basic details
            compilationTitle.text = compilation.title
            compilationDescription.text = compilation.description
            // Load images
            authorPhoto.loadWithGlide(compilation.authorPhoto, R.drawable.ic_avatar_icon)
            compilationPhoto.loadWithGlide(compilation.photo)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(compilation: CompilationItemUi)
    }

    object CompilationComparator : DiffUtil.ItemCallback<CompilationItemUi>() {
        override fun areItemsTheSame(
            oldItem: CompilationItemUi,
            newItem: CompilationItemUi
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CompilationItemUi,
            newItem: CompilationItemUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}