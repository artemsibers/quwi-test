package com.quwi.testapp.data.models

sealed class AuthStatus{
    class InitialState : AuthStatus()
    class Authorized(
        val token: String,
        val user: User
    ) : AuthStatus()
    class NotAuthorized : AuthStatus()
    class Error(
        val error: String
    ) : AuthStatus()
}
