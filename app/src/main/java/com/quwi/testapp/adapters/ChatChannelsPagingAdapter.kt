package com.quwi.testapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.quwi.testapp.data.models.ChatChannelInfo
import com.quwi.testapp.databinding.ChatListItemBinding

class ChatChannelsPagingAdapter(private val userId: Int): PagingDataAdapter<ChatChannelInfo, ChatChannelViewHolder>(CHAT_CHANNEL_COMPARATOR) {

    override fun onBindViewHolder(holder: ChatChannelViewHolder, position: Int) {
        val chatInfo = getItem(position)
        if (chatInfo != null) {
            holder.bind(chatInfo, userId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatChannelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChatChannelViewHolder(ChatListItemBinding.inflate(layoutInflater, parent, false))
    }

    companion object {
        private val CHAT_CHANNEL_COMPARATOR = object : DiffUtil.ItemCallback<ChatChannelInfo>() {
            override fun areItemsTheSame(oldItem: ChatChannelInfo, newItem: ChatChannelInfo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ChatChannelInfo, newItem: ChatChannelInfo): Boolean =
                oldItem == newItem
        }
    }
}