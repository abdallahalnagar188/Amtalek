package eramo.amtalek.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.AllBrokersPropertysResponse
import eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.DataX
import eramo.amtalek.domain.repository.BrokersPropertiesRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrokersPropertiesRepoImpl(private val api: AmtalekApi) : BrokersPropertiesRepo {
    override suspend fun getBrokersPropertiesFromRemote(id: Int): Flow<Resource<AllBrokersPropertysResponse>> {
        return flow {
            val result = toResultFlow {
                api.getBrokersProperties(page = 1, id = id)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override  fun getAllBrokersPropertiesFromRemote(id: Int): PagingSource<Int, DataX> {
        return object : PagingSource<Int, DataX>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
                return try {
                    val currentPage = params.key ?: 1

                    // Fetching the data from the API
                    val response = api.getBrokersProperties(page = currentPage,id = id)
                    val data = response.body()?.data?.original?.data

                    // Returning the loaded result
                    LoadResult.Page(
                        data = data?: emptyList(),
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (data.isNullOrEmpty()) null else currentPage + 1
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }


            override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }
    }

}