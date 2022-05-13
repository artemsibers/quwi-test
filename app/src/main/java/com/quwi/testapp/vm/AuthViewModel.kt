package com.quwi.testapp.vm

import android.app.Application
import androidx.lifecycle.*
import com.quwi.testapp.SharedPrefsHelper
import com.quwi.testapp.data.AuthRepository
import com.quwi.testapp.data.api.AuthInterceptor
import com.quwi.testapp.data.models.AuthResponse
import com.quwi.testapp.data.models.AuthStatus
import com.quwi.testapp.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repo = AuthRepository.provideRepository()
    private val prefs = SharedPrefsHelper(app)

    private val _authStatus: MutableLiveData<AuthStatus> = MutableLiveData(AuthStatus.InitialState())
    val authStatus: LiveData<AuthStatus> = _authStatus

    var currentUser: User? = null
        private set


    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resp = repo.login(username, password)
            when (resp) {
                is AuthResponse.Login -> {
                    prefs.setAuthToken(resp.token)
                    AuthInterceptor.setToken(resp.token)
                    currentUser = resp.appInit.user
                    _authStatus.postValue(AuthStatus.Authorized(resp.token, resp.appInit.user))
                }
                is AuthResponse.Failed -> {
                    AuthInterceptor.setToken(null)
                    prefs.clearAuthToken()
                    currentUser = null
                    _authStatus.postValue(AuthStatus.Error(resp.errors.joinToString("\n")))
                }
                is AuthResponse.NetworkError -> {
                    AuthInterceptor.setToken(null)
                    currentUser = null
                    prefs.clearAuthToken()
                    _authStatus.postValue(AuthStatus.Error(resp.error))
                }
                else -> {}
            }
        }
    }

    fun authWithToken() {
        prefs.getAuthToken()?.let { token ->
            viewModelScope.launch(Dispatchers.IO) {
                AuthInterceptor.setToken(token)
                val resp = repo.init()
                when (resp) {
                    is AuthResponse.Init -> {
                        _authStatus.postValue(AuthStatus.Authorized(token, resp.user))
                        currentUser = resp.user
                    }
                    is AuthResponse.Failed -> {
                        AuthInterceptor.setToken(null)
                        currentUser = null
                        prefs.clearAuthToken()
                        _authStatus.postValue(AuthStatus.Error(resp.errors.joinToString("\n")))
                    }
                    is AuthResponse.NetworkError -> {
                        AuthInterceptor.setToken(null)
                        currentUser = null
                        prefs.clearAuthToken()
                        _authStatus.postValue(AuthStatus.Error(resp.error))
                    }
                    else -> {}
                }
            }
        }?:run {
            _authStatus.postValue(AuthStatus.NotAuthorized())
            currentUser = null
        }
    }

    fun logout(anywhere: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val resp = repo.logout(anywhere)
            when (resp) {
                is AuthResponse.Logout -> {
                    AuthInterceptor.setToken(null)
                    prefs.clearAuthToken()
                    currentUser = null
                    _authStatus.postValue(AuthStatus.NotAuthorized())
                }
                is AuthResponse.NetworkError -> {
                    _authStatus.postValue(AuthStatus.Error(resp.error))
                }
                else -> {}
            }
        }
    }
}