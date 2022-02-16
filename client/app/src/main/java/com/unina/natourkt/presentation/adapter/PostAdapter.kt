package com.unina.natourkt.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.databinding.PostItemBinding
import com.unina.natourkt.domain.model.post.Post

class PostAdapter(
    var posts: List<Post>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(layoutInflater, parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class PostViewHolder(val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {

                authorName.text = post.user.username
                description.text = post.description

                // If the user photo is present, then load with Glide
                Glide.with(this.root)
                    .load(post.user.photo)
                    .fallback(R.drawable.ic_avatar_svgrepo_com)
                    .into(authorPhoto)


                // Map post photos to SlideModel type
                val imageList = ArrayList<SlideModel>()
                post.photos.mapTo(imageList) {
                    SlideModel(it.photo)
                }

                // Load photos in the slider
                imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
            }
        }
    }
}