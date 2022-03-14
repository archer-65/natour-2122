package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.ErrorHandler
import com.unina.natourkt.databinding.ItemLoadBinding

/**
 * The ItemLoadStateAdapter is a common class used by Paging Adapters
 * of the Paging 3 library, used for displaying items based on LoadState of PagedList
 */
class ItemLoadStateAdapter : LoadStateAdapter<ItemLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(
            ItemLoadBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(val binding: ItemLoadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binding function, nothing too special here, manage Error wrapped by
         * LoadState, our [ErrorHandler] manages the exception, associating
         * one CustomMessage, this one could be used for Error Messages
         */
        fun bind(loadState: LoadState) = with(binding) {
            // State Views visibility
            progressBar.isVisible = loadState is LoadState.Loading
            errorLayout.isVisible =
                loadState is LoadState.Error && ErrorHandler.handleException(loadState.error) is DataState.Cause.NetworkError
        }
    }
}