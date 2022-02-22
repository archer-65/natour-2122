package com.unina.natourkt.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.common.GlideApp
import com.unina.natourkt.databinding.PostItemBinding
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState

/**
 * Implementation of PagingDataAdapter for [PostItemUiState] (posts on home screen)
 */
class PostAdapter :
    PagingDataAdapter<PostItemUiState, PostAdapter.PostViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(layoutInflater, parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class PostViewHolder(val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binder function, relies on [Glide] and [SlideModel]
         */
        fun bind(post: PostItemUiState) {
            binding.apply {

                // Set basic details
                authorName.text = post.authorUsername
                routeName.text = post.routeTitle
                description.text = post.description

                // If the user photo is present, then load with Glide
                GlideApp.with(this.root)
                    .load(post.authorPhoto)
                    .fallback(R.drawable.ic_avatar_svgrepo_com)
                    .into(authorPhoto)


                // Map post photos to SlideModel type
                val imageList = ArrayList<SlideModel>()
                post.photos.mapTo(imageList) {
                    SlideModel(it)
                }

                // Load photos in the slider
                imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PostItemUiState>() {
        override fun areItemsTheSame(oldItem: PostItemUiState, newItem: PostItemUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PostItemUiState,
            newItem: PostItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }
}