package eramo.amtalek.presentation.ui.main.broker

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.broker.entity.DataX

class BrokersPagingSource(
    private val brokerApi: AmtalekApi
) : PagingSource<Int, DataX>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
        val page = params.key ?: 1
        return try {
            val response = brokerApi.getBrokers(page)
            val brokers = response.data.original.data
            LoadResult.Page(
                data = brokers,
                prevKey = null, // Only paging forward.
                nextKey = if (brokers.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
        return null // Optional: handle refresh key if needed.
    }
}
