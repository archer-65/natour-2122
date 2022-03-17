package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.databinding.CompilationDialogItemBinding

class CompilationDialogAdapter(private val listener: OnItemCLickListener) :
    ListAdapter<CompilationDialogItemUi, CompilationDialogAdapter.CompilationHolder>(
        CompilationComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CompilationHolder(
            CompilationDialogItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(
        holder: CompilationHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

    inner class CompilationHolder(val binding: CompilationDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onCompilationSelection(item)
                    }
                }
            }
        }

        fun bind(compilation: CompilationDialogItemUi) {
            binding.compilationTitle.text = compilation.title
            binding.authorPhoto.loadWithGlide(compilation.photo)
        }
    }

    interface OnItemCLickListener {
        fun onCompilationSelection(compilation: CompilationDialogItemUi)
    }

    object CompilationComparator : DiffUtil.ItemCallback<CompilationDialogItemUi>() {
        override fun areItemsTheSame(
            oldItem: CompilationDialogItemUi,
            newItem: CompilationDialogItemUi
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CompilationDialogItemUi,
            newItem: CompilationDialogItemUi
        ) = oldItem.equals(newItem)
    }
}