package com.quwi.testapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.quwi.testapp.data.api.QuwiAPI
import com.quwi.testapp.data.models.ChatChannelInfo
import kotlinx.coroutines.flow.Flow

class ChatRepository {
    private val api: QuwiAPI = QuwiAPI.create()

    fun getChatChannelsF(): Flow<PagingData<ChatChannelInfo>> {
        return Pager(PagingConfig(pageSize = 10)) {
            ChatChannelsSource(api)
        }.flow
    }

    companion object  {
        @Volatile
        private var INSTANCE: ChatRepository? = null

        fun provideRepository(): ChatRepository {
            synchronized(this) {
                return INSTANCE ?: ChatRepository().also { INSTANCE = it }
            }
        }
    }
}