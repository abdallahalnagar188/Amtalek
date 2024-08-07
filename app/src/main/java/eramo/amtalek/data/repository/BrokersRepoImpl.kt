package eramo.amtalek.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.broker.entity.DataX
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.domain.repository.BrokersRepo

class BrokersRepoImpl(private val apiService: AmtalekApi) : BrokersRepo {
    override suspend fun getBrokersFromRemote(): BrokersResponse = apiService.getBrokers(1)

    override fun getBrokersPagingSource(): PagingSource<Int, DataX> {
        return object : PagingSource<Int, DataX>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
                val page = params.key ?: 1
                return try {
                    val response = apiService.getBrokers(page)
                    val brokers = response.data.original.data
                    LoadResult.Page(
                        data = brokers,
                        prevKey = null, // You can handle previous pages if necessary
                        nextKey = if (brokers.isEmpty()) null else page + 1
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
                return null // Handle refresh key if needed
            }
        }
    }
}
