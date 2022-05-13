package com.quwi.testapp.adapters

import android.graphics.Color
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.quwi.testapp.CT_CUSTOM
import com.quwi.testapp.CT_PM
import com.quwi.testapp.R
import com.quwi.testapp.data.models.ChatChannelInfo
import com.quwi.testapp.databinding.ChatListItemBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatChannelViewHolder(private val viewBinding: ChatListItemBinding): RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(chatInfo: ChatChannelInfo, userId: Int) {
        var chatName = "Unknown"
        var avatarUrl: String? = null
        var lastUsername = ""

        when (chatInfo.type) {
            CT_PM -> {
                chatInfo.partner?.let { chatName = it.name }
                chatInfo.partner?.avatar?.let { avatarUrl = it }
                if (chatInfo.lastMessage?.userId == userId) {
                    lastUsername = itemView.context.getString(R.string.you)
                }
            }
            CT_CUSTOM -> {
                chatInfo.customInfo?.let { chatName = it.name }
                chatInfo.customInfo?.logoUrl?.let { avatarUrl = it }
                chatInfo.lastMessage?.let {
                    lastUsername = if (it.userId == userId)
                        itemView.context.getString(R.string.you)
                        else "${it.user.name}:"
                }
            }
        }

        avatarUrl?.let {
            viewBinding.chatImage.visibility = View.VISIBLE
            viewBinding.chatLetter.visibility = View.GONE
            Glide.with(viewBinding.chatImage)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewBinding.chatImage)
        }?: run {
            setColors(chatName)
            viewBinding.chatLetter.text =
                chatName.split(" ").take(2).joinToString("") { it[0].uppercase() }
            viewBinding.chatImage.visibility = View.GONE
            viewBinding.chatLetter.visibility = View.VISIBLE
        }
        viewBinding.tvName.text = chatName
        viewBinding.tvLastUsername.text = lastUsername
        viewBinding.tvLastMessage.text = chatInfo.lastMessage?.text?:itemView.resources.getString(R.string.no_messages)

        viewBinding.tvDateTime.text =
            chatInfo.lastMessage?.let {
                if (DateUtils.isToday(chatInfo.date.time))
                    SimpleDateFormat("H:mm", Locale.getDefault()).format(it.date)
                else SimpleDateFormat("d MMM", Locale.getDefault()).format(it.date)
            }?:""

        if (chatInfo.lastMessage != null && chatInfo.lastMessage.userId == userId) {
            if (chatInfo.lastMessage.isRead > 0) {
                viewBinding.tvDateTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_all, 0, 0, 0)
            } else {
                viewBinding.tvDateTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0)
            }
        } else {
            viewBinding.tvDateTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }

        viewBinding.ivPinned.visibility =
            if (chatInfo.pinToTop) { View.VISIBLE } else { View.GONE }

    }

    private fun setColors(name: String){
        val hash = name.hashCode().toFloat()
        val fg = Color.HSVToColor(floatArrayOf((360f*(hash-Int.MIN_VALUE)/(Int.MAX_VALUE.toFloat()-Int.MIN_VALUE)), 1f, 0.5f))
        val bg = Color.HSVToColor(floatArrayOf((360f*(hash-Int.MIN_VALUE)/(Int.MAX_VALUE.toFloat()-Int.MIN_VALUE)), 0.5f, 1f))
        viewBinding.chatLetter.setTextColor(fg)
        viewBinding.chatLetter.setBackgroundColor(bg)
    }
}