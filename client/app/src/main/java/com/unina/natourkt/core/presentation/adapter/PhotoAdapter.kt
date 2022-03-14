package com.unina.natourkt.core.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.databinding.NewPhotoItemBinding

class PhotoAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Uri, PhotoAdapter.PhotoHolder>(PhotoComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PhotoHolder(
            NewPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

    inner class PhotoHolder(val binding: NewPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.removeButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRemoveClick(position)
                }
            }
        }

        fun bind(photo: Uri) {
            binding.imagePreview.setImageURI(photo)
        }
    }

    interface OnItemClickListener {
        fun onRemoveClick(position: Int)
    }

    object PhotoComparator : DiffUtil.ItemCallback<Uri>() {

        override fun areItemsTheSame(oldItem: Uri, newItem: Uri) =
            oldItem.equals(newItem)

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri) =
            oldItem.equals(newItem)
    }
}