package com.quwi.testapp.data.models

import com.google.gson.annotations.SerializedName

sealed class AuthResponse {
    class Login(
        @SerializedName("token")
        val token: String,
        @SerializedName("app_init")
        val appInit: Init
    ) : AuthResponse()

    class Init(
        @SerializedName("user")
        val user: User,
    ) : AuthResponse()

    class Logout : AuthResponse()

    class Failed(val errors: List<String>) : AuthResponse()
    class NetworkError(val error: String) : AuthResponse()
}
