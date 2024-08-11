package eramo.amtalek.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.domain.repository.AllPropertyRepo

class AllPropertiesRepoImpl(private val apiService: AmtalekApi) : AllPropertyRepo {

    override suspend fun getAllPropertiesFromRemote(): AllPropertyResponse {
        return apiService.getAllFeaturedProperties()
    }

    override fun getAllPropertiesPagingSource(): PagingSource<Int, DataX> {
        return object : PagingSource<Int, DataX>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
                return try {
                    val currentPage = params.key ?: 1

                    // Fetching the data from the API
                    val response = apiService.getAllFeaturedProperties()
                    val data = response.data?.original?.data

                    // Returning the loaded result
                    LoadResult.Page(
                        data = data?: emptyList(),
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (data?.isEmpty() == true) null else currentPage + 1
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
