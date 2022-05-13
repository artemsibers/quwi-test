package com.quwi.testapp.data.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class ChatChannelsResponse(
    @SerializedName("channels")
    val channels: List<ChatChannelInfo>
)

data class ChatChannelInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("id_partner")
    val partnerId: Int?,
    @SerializedName("dta_change_msg")
    val date: Date,
    @SerializedName("message_last")
    val lastMessage: LastMessage?,
    @SerializedName("pin_to_top")
    val pinToTop: Boolean,

    @SerializedName("custom_info")
    val customInfo: CustomInfo?,
) {
    var partner: User? = null
}

data class LastMessage(
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_user")
    val userId: Int,
    @SerializedName("user")
    val user: LastMessageUser,
    @SerializedName("dta_create")
    val date: Date,
    @SerializedName("is_read")
    val isRead: Int,
    @SerializedName("text")
    val text: String

)

data class LastMessageUser(
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)

// "custom_info"
data class CustomInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_url")
    val logoUrl: String?,
)