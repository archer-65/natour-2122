package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.databinding.ItemLoadBinding

/**
 * The ItemLoadStateAdapter is a common class used by Paging Adapters
 * of the Paging 3 library, used for displaying items based on LoadState of PagedList
 */
class ItemLoadStateAdapter : LoadStateAdapter<ItemLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ItemLoadStateAdapter.LoadStateViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLoadBinding.inflate(layoutInflater, parent, false)

        return LoadStateViewHolder(binding)
    }

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

            // Error management
            if (loadState is LoadState.Error) {
                val errorHandler = ErrorHandler()
                val customMsg = errorHandler.handleException<Throwable>(loadState.error)

                val message = when (customMsg) {
                    is DataState.CustomMessages.NotFound -> "Nothing here"
                    else -> "Unknown Error"
                }

                errorMsg.text = message
            }

            // State Views visibility
            progressBar.isVisible = loadState is LoadState.Loading
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}