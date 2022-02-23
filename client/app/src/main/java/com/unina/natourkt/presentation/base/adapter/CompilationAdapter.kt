package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.databinding.CompilationItemBinding
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState

/**
 * Implementation of PagingDataAdapter for [CompilationItemUiState] (compilations on profile screen)
 */
class CompilationAdapter :
    PagingDataAdapter<CompilationItemUiState, CompilationAdapter.CompilationViewHolder>(
        DiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompilationViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CompilationItemBinding.inflate(layoutInflater, parent, false)

        return CompilationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompilationAdapter.CompilationViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CompilationViewHolder(val binding: CompilationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binder function, relies on [Glide]
         */
        fun bind(compilation: CompilationItemUiState) {
            binding.apply {

                // Set basic details
                compilationTitle.text = compilation.title
                compilationDescription.text = compilation.description

                // If the user photo is present, then load with Glide
                GlideApp.with(this.root)
                    .load(compilation.authorPhoto)
                    .error(R.drawable.ic_avatar_svgrepo_com)
                    .into(authorPhoto)

                // If the compilation photo is present, then load with Glide
                GlideApp.with(this.root)
                    .load(compilation.photo)
                    .into(compilationPhoto)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<CompilationItemUiState>() {
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