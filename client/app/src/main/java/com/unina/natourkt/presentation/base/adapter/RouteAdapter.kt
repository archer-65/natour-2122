package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.common.loadWithGlide
import com.unina.natourkt.databinding.RouteItemBinding
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState

/**
 * Implementation of PagingDataAdapter for [RouteItemUiState] (posts on routes screen)
 */
class RouteAdapter(private val listener: OnItemClickListener) :
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
         * Binder function, relies on [Glide] to display only one preview photo
         */
        fun bind(route: RouteItemUiState) {
            binding.apply {

                routeTitle.text = route.title

                routePhoto.loadWithGlide(route.previewPhoto, R.drawable.media_placeholder)

                // Select the correct difficulty drawable, then set it
                val difficulty = when (route.avgDifficulty) {
                    1 -> R.drawable.difficulty_easy
                    2 -> R.drawable.difficulty_medium
                    3 -> R.drawable.difficulty_hard
                    else -> R.drawable.difficulty_label
                }
                iconDifficulty.setImageResource(difficulty)

                // Accessibility icon visibility based on route's boolean attribute
                iconDisabled.isVisible = route.disabilityFriendly
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(route: RouteItemUiState)
        //fun onOptionsClick(post: PostItemUiState, position: Int)
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