package com.quwi.testapp

import android.content.Context
import androidx.core.content.edit

class SharedPrefsHelper(private val context: Context) {

    private val sharedPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun getAuthToken(): String? {
        return sharedPrefs.getString(AUTH_TOKEN, null)
    }

    fun setAuthToken(token: String) {
        sharedPrefs.edit(commit = true) {
            putString(AUTH_TOKEN, token)
        }
    }

    fun clearAuthToken() {
        sharedPrefs.edit(commit = true) {
            remove(AUTH_TOKEN)
        }
    }

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
    }
}