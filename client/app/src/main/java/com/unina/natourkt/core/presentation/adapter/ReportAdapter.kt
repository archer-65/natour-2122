package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.CompilationItemBinding
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import com.unina.natourkt.core.presentation.model.ReportItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide
import com.unina.natourkt.databinding.ReportItemBinding

/**
 * Implementation of PagingDataAdapter for [CompilationItemUi]
 */
class ReportAdapter(val listener: OnItemClickListener) :
    PagingDataAdapter<ReportItemUi, ReportAdapter.ReportViewHolder>(ReportComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReportViewHolder(
            ReportItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ReportAdapter.ReportViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ReportViewHolder(val binding: ReportItemBinding) :
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

        fun bind(report: ReportItemUi) = with(binding) {
            // Set basic details
            reportTitle.text = report.title
            reportedRouteTitle.text = report.reportedRoute.routeTitle
            // Load images
            reportAuthorPhoto.loadWithGlide(report.authorPhoto, R.drawable.ic_avatar_icon)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(report: ReportItemUi)
    }

    object ReportComparator : DiffUtil.ItemCallback<ReportItemUi>() {
        override fun areItemsTheSame(
            oldItem: ReportItemUi,
            newItem: ReportItemUi
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReportItemUi,
            newItem: ReportItemUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}