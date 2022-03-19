package com.unina.natourkt.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.model.ChatGenericUi
import com.unina.natourkt.core.presentation.model.DateItemUi
import com.unina.natourkt.core.presentation.model.MessageItemUi
import com.unina.natourkt.core.presentation.model.MessageType
import com.unina.natourkt.core.presentation.util.format
import com.unina.natourkt.databinding.MessageItemDateBinding
import com.unina.natourkt.databinding.MessageItemMeBinding
import com.unina.natourkt.databinding.MessageItemOtherBinding
import java.time.LocalDate

class MessageAdapter : ListAdapter<ChatGenericUi, RecyclerView.ViewHolder>(MessageComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (MessageType.create(viewType)) {
            MessageType.TYPE_ME -> {
                MessageMeHolder(
                    MessageItemMeBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            MessageType.TYPE_OTHER -> {
                MessageOtherHolder(
                    MessageItemOtherBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            MessageType.TYPE_DATE -> {
                MessageDateHolder(
                    MessageItemDateBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageMeHolder -> {
                holder.bind(getItem(position) as MessageItemUi)
            }
            is MessageOtherHolder -> {
                holder.bind(getItem(position) as MessageItemUi)
            }
            is MessageDateHolder -> {
                holder.bind(getItem(position) as DateItemUi)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.id
    }

    inner class MessageMeHolder(val binding: MessageItemMeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageItemUi) = with(binding) {
            textGchatTimestampMe.text = message.sentOn.toLocalTime().format()
            textGchatMessageMe.text = message.content
        }
    }

    inner class MessageOtherHolder(val binding: MessageItemOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageItemUi) = with(binding) {
            textGchatTimestampOther.text = message.sentOn.toLocalTime().format()
            textGchatMessageOther.text = message.content
        }
    }

    inner class MessageDateHolder(val binding: MessageItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: DateItemUi) = with(binding) {
            dateText.text = getDate(message.date)
        }

        fun getDate(messageDate: LocalDate): String = with(binding) {
            return if (messageDate.isEqual(LocalDate.now())) {
                root.context.getString((R.string.today_chat))
            } else if (messageDate.isEqual(LocalDate.now().minusDays(1))) {
                root.context.getString(R.string.yesterday_chat)
            } else {
                messageDate.format()
            }
        }
    }

    object MessageComparator : DiffUtil.ItemCallback<ChatGenericUi>() {
        override fun areItemsTheSame(oldItem: ChatGenericUi, newItem: ChatGenericUi) =
            oldItem.equals(newItem)

        override fun areContentsTheSame(oldItem: ChatGenericUi, newItem: ChatGenericUi) =
            oldItem.equals(newItem)
    }
}