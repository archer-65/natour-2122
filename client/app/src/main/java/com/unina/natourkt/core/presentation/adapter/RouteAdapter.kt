package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unina.natourkt.R
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.databinding.RouteItemBinding
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide

/**
 * Implementation of PagingDataAdapter for [RouteItemUi] (posts on routes screen)
 */
class RouteAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<RouteItemUi, RouteAdapter.RouteViewHolder>(DiffUtilCallback()) {

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

            binding.chipSave.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onSaveClick(item)
                    }
                }
            }
        }

        /**
         * Binder function, relies on [Glide] to display only one preview photo
         */
        fun bind(route: RouteItemUi) {
            binding.apply {

                routeTitle.text = route.title

                routePhoto.loadWithGlide(route.previewPhoto, R.drawable.media_placeholder)

                // Select the correct difficulty drawable, then set it
                val difficulty = when (route.avgDifficulty) {
                    Difficulty.EASY -> R.drawable.difficulty_easy
                    Difficulty.MEDIUM -> R.drawable.difficulty_medium
                    Difficulty.HARD -> R.drawable.difficulty_hard
                    else -> R.drawable.difficulty_easy
                }
                iconDifficulty.setImageResource(difficulty)

                // Accessibility icon visibility based on route's boolean attribute
                iconDisabled.isVisible = route.disabilityFriendly
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(route: RouteItemUi)
        fun onSaveClick(route: RouteItemUi)
        //fun onOptionsClick(post: PostItemUiState, position: Int)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<RouteItemUi>() {
        override fun areItemsTheSame(
            oldItem: RouteItemUi,
            newItem: RouteItemUi
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RouteItemUi,
            newItem: RouteItemUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}