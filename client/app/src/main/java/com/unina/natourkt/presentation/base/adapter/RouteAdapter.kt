package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unina.natourkt.R
import com.unina.natourkt.databinding.RouteItemBinding
import com.unina.natourkt.presentation.routes.RouteItemUiState

class RouteAdapter :
    PagingDataAdapter<RouteItemUiState, RouteAdapter.RouteViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RouteItemBinding.inflate(layoutInflater, parent, false)

        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteAdapter.RouteViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class RouteViewHolder(val binding: RouteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(route: RouteItemUiState) {
            binding.apply {

                routeTitle.text = route.title

                Glide.with(this.root)
                    .load(route.previewPhoto)
                    .fallback(R.drawable.ic_accessible_22)
                    .into(routePhoto)

                val difficulty = when (route.avgDifficulty) {
                    1 -> {
                        R.drawable.difficulty_easy
                    }
                    2 -> {
                        R.drawable.difficulty_medium
                    }
                    3 -> {
                        R.drawable.difficulty_hard
                    }
                    else -> {
                        R.drawable.difficulty_label
                    }
                }

                iconDifficulty.setImageResource(difficulty)

                iconDisabled.isVisible = route.disabledFriendly
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<RouteItemUiState>() {
        override fun areItemsTheSame(
            oldItem: RouteItemUiState,
            newItem: RouteItemUiState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RouteItemUiState,
            newItem: RouteItemUiState
        ): Boolean {
            return oldItem == newItem
        }
    }
}