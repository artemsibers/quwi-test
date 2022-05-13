package com.quwi.testapp.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
//    @SerializedName("nick")
//    val nick: String,
    @SerializedName("avatar_url")
    val avatar: String,
//    val dta_create: String,     // "2022-05-05 14:25:39"
//    val timezone_offset: Int,   // 180
//    val is_online: Int,         // 1
//    val is_chat_email_notification: Boolean,
//    val dta_activity: String,   // "2022-05-09 08:00:38"
//    val is_active: Boolean

)
