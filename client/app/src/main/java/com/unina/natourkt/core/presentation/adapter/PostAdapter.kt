package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.core.util.GlideApp
import com.unina.natourkt.databinding.PostItemBinding
import com.unina.natourkt.core.presentation.model.PostItemUi

/**
 * Implementation of PagingDataAdapter for [PostItemUi] (posts on home screen)
 */
class PostAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<PostItemUi, PostAdapter.PostViewHolder>(DiffUtilCallback()) {

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
         * Binder function, relies on [Glide] and [SlideModel]
         */
        fun bind(post: PostItemUi) {
            binding.apply {

                // Set basic details
                authorName.text = post.authorUsername
                routeName.text = post.routeTitle
                description.text = post.description

                // If the user photo is present, then load with Glide
                GlideApp.with(this.root)
                    .load(post.authorPhoto)
                    .fallback(R.drawable.ic_avatar_icon)
                    .into(authorPhoto)

                // Load photos in the slider
                val imageList = post.photos.map { SlideModel(it) }
                imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: PostItemUi)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PostItemUi>() {
        override fun areItemsTheSame(oldItem: PostItemUi, newItem: PostItemUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PostItemUi,
            newItem: PostItemUi
        ): Boolean {
            return oldItem == newItem
        }

    }
}