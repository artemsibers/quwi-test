package com.quwi.testapp.data

import com.quwi.testapp.data.api.QuwiAPI
import com.quwi.testapp.data.models.AuthResponse
import com.quwi.testapp.data.models.LoginRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

class AuthRepository {
    private val api: QuwiAPI = QuwiAPI.create()

    suspend fun login(username: String, password: String): AuthResponse {
        try {
            val resp = api.login(LoginRequest(username, password))
            if (resp.isSuccessful) {
                return resp.body()?:AuthResponse.NetworkError("Unknown error")
            } else {
                return getErrorStatus(resp.errorBody())
            }
        } catch (e: Exception) {
            return AuthResponse.NetworkError("Network error: ${e.message}")
        }
    }

    suspend fun init(): AuthResponse {
        try {
            val resp = api.auth()
            if (resp.isSuccessful) {
                return resp.body()?:AuthResponse.NetworkError("Unknown error")
            } else {
                return getErrorStatus(resp.errorBody())
            }
        } catch (e: Exception) {
            return AuthResponse.NetworkError("Network error: ${e.message}")
        }
    }

    suspend fun logout(anywhere: Boolean): AuthResponse {
        val requestBody = "{\"anywhere\":$anywhere}".toRequestBody("application/json".toMediaTypeOrNull())
        val resp = api.logout(requestBody)
        if (resp.isSuccessful) {
            return AuthResponse.Logout()
        } else {
            return getErrorStatus(resp.errorBody())
        }
    }

    private fun getErrorStatus(errBody: ResponseBody?):AuthResponse {
        errBody?.let {
            val errText = it.string()
            val errList = mutableListOf<String>()
            val errJson = JSONObject(errText)
            val errMessages = errJson.getJSONObject("first_errors")
            errMessages.keys().forEach { key ->
                errList.add("$key : ${errMessages.get(key)}")
            }
            return AuthResponse.Failed(errList)
        }
        return AuthResponse.NetworkError("Unknown network error")
    }

    companion object {
        private var INSTANCE: AuthRepository? = null

        fun provideRepository(): AuthRepository {
            synchronized(this) {
                return INSTANCE ?: AuthRepository().also { INSTANCE = it }
            }
        }
    }
}