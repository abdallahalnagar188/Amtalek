package eramo.amtalek.presentation.ui.main.broker

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.DataX
import javax.inject.Inject

//class AllBrokerPropertiesPagingSource @Inject constructor(
//    private val amtalekApi: AmtalekApi,
//    private val id: Int
//) : PagingSource<Int, DataX>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
//        val page = params.key ?: 1
//        return try {
//            val response = amtalekApi.getBrokersProperties(page = page, id = id)
//            val data = response.body()?.data?.original?.data
//            LoadResult.Page(
//                data = data?: emptyList(),
//                prevKey = null, // No previous page
//                nextKey = if (data.isNullOrEmpty()) null else page + 1 // Next page key
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
//        return null
//        // Return the initial key used when the list was first loaded.
////        return state.anchorPosition?.let { anchorPosition ->
////            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
////                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
////        }
//    }
//}
