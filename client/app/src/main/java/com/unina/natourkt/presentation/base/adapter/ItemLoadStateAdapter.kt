package com.unina.natourkt.presentation.base.adapter

import android.accounts.NetworkErrorException
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkError
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
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
            val errorHandler = ErrorHandler()
            // Error management
//            if (loadState is LoadState.Error) {
//
//                val error = errorHandler.handleException(loadState.error)
//                errorLayout.isVisible = error is DataState.CustomMessage.NetworkError
//            }

            // State Views visibility
            progressBar.isVisible = loadState is LoadState.Loading
            errorLayout.isVisible = loadState is LoadState.Error && errorHandler.handleException(loadState.error) is DataState.CustomMessage.NetworkError
        }
    }
}