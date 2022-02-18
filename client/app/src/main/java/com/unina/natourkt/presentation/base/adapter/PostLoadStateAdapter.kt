package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.databinding.PostItemLoadBinding

class PostLoadStateAdapter : LoadStateAdapter<PostLoadStateAdapter.PostLoadViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PostLoadStateAdapter.PostLoadViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostItemLoadBinding.inflate(layoutInflater, parent, false)

        return PostLoadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class PostLoadViewHolder(val binding: PostItemLoadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = with(binding) {
            if (loadState is LoadState.Error) {
                val errorHandler = ErrorHandler()
                val customMsg = errorHandler.handleException<Throwable>(loadState.error)

                val message = when (customMsg) {
                    is DataState.CustomMessages.NotFound -> "Nothing here"
                    else -> "Unknown Error"
                }

                errorMsg.text = message
            }
            progressBar.isVisible = loadState is LoadState.Loading
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}