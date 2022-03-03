package com.unina.natourkt.presentation.base.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.databinding.NewPhotoItemBinding
import java.util.*

class NewPhotoAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Uri, NewPhotoAdapter.NewPhotoHolder>(NewPhotosComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewPhotoHolder(
            NewPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: NewPhotoHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

    inner class NewPhotoHolder(val binding: NewPhotoItemBinding) :
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

    object NewPhotosComparator : DiffUtil.ItemCallback<Uri>() {

        override fun areItemsTheSame(oldItem: Uri, newItem: Uri) =
            oldItem.equals(newItem)

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri) =
            oldItem.equals(newItem)
    }
}