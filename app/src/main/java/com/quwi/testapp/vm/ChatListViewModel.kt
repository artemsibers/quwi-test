package com.quwi.testapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.quwi.testapp.data.ChatRepository
import com.quwi.testapp.data.models.ChatChannelInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus

class ChatListViewModel : ViewModel() {

    private val repo = ChatRepository.provideRepository()
    private val refresh = MutableStateFlow(true)

    val chatChannelsF : Flow<PagingData<ChatChannelInfo>> =
        repo.getChatChannelsF().cachedIn(viewModelScope + Dispatchers.IO)
}