package eramo.amtalek.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.news.allnews.AllNewsResponse
import eramo.amtalek.data.remote.dto.myHome.news.allnews.Data
import eramo.amtalek.data.remote.dto.myHome.news.allnews.DataX
import eramo.amtalek.domain.repository.AllNewsRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AllNewsRepoImpl(private val apiService: AmtalekApi): AllNewsRepo {
    override suspend fun getAllNews(): Flow<Resource<AllNewsResponse>> {

        return flow {
            val result = toResultFlow {
                apiService.getAllNews(1)
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

    override fun getAllNewsPagingSource(): PagingSource<Int, DataX> {
        return object : PagingSource<Int, DataX>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
                val page = params.key ?: 1
                return try {
                    val response = apiService.getAllNews(page)
                    val news = response.body()?.data?.original?.data
                    LoadResult.Page(
                        data = news?: emptyList(),
                        prevKey = null, // You can handle previous pages if necessary
                        nextKey = if (news?.isEmpty() == true) null else page + 1
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