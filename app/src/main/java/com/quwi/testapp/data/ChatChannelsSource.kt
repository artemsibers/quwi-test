package com.quwi.testapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.quwi.testapp.CT_PM
import com.quwi.testapp.data.api.QuwiAPI
import com.quwi.testapp.data.models.ChatChannelInfo
import java.lang.Exception

class ChatChannelsSource(
    private val service: QuwiAPI,

) : PagingSource<Int, ChatChannelInfo>() {

    override fun getRefreshKey(state: PagingState<Int, ChatChannelInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatChannelInfo> {
        val page = params.key ?: STARTING_PAGE_INDEX
        val resp = service.getChatChannelsPage(page, params.loadSize)
        if (resp.isSuccessful) {
            val pageCount = resp.headers()["X-Pagination-Page-Count"]?.toInt()?:0
            val currentPage = resp.headers()["X-Pagination-Current-Page"]?.toInt()?:0
            val nextKey = when {
                (pageCount > currentPage) -> currentPage + 1
                resp.body()!!.channels.isEmpty() -> null
                else -> null
            }

            val channels =  resp.body()!!.channels
            val partnerIds = channels
                .filter { it.partnerId != null }
                .map { it.partnerId }
                .distinct()
                .joinToString()
            val partners = service.getUsersDetails(partnerIds).users.associateBy { it.id }

            channels.map { ch ->
                if (ch.type == CT_PM) {
                    ch.partner = partners[ch.partnerId]
                }
            }

            return LoadResult.Page(
                data = channels,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = nextKey
            )
        }
        return LoadResult.Error(Exception("ERR"))
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}