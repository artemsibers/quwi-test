package com.quwi.testapp.data.api

import com.google.gson.GsonBuilder
import com.quwi.testapp.BASE_URL
import com.quwi.testapp.data.models.AuthResponse
import com.quwi.testapp.data.models.ChatChannelsResponse
import com.quwi.testapp.data.models.LoginRequest
import com.quwi.testapp.data.models.UsersResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface QuwiAPI {

    @Headers(
        "Client-Device: android",
        "Client-Timezone-Name: Europe/Helsinki"
    )
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse.Login>

    @Headers(
        "Client-Device: android",
        "Client-Timezone-Name: Europe/Helsinki",
    )
    @GET("auth/init")
    suspend fun auth(): Response<AuthResponse.Init>

    @POST("auth/logout")
    suspend fun logout(@Body body : RequestBody): Response<Any>

    @GET("chat-channels")
    suspend fun getChatChannelsPage(
        @Query("page") page: Int,
        @Query("per-page") limit: Int = 20
    ): Response<ChatChannelsResponse>

    @GET("users/foreign")
    suspend fun getUsersDetails(@Query("ids") ids: String): UsersResponse

    companion object {
        fun create(): QuwiAPI {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(AuthInterceptor)
                .addInterceptor(logging)
                .build()

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(QuwiAPI::class.java)
        }
    }
}