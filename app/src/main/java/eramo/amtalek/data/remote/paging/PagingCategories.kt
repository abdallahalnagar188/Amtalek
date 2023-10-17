package eramo.amtalek.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.util.PAGING_PER_PAGE
import eramo.amtalek.util.PAGING_START_INDEX
import eramo.amtalek.util.UserUtil
import retrofit2.HttpException
import java.io.IOException

class PagingCategories(
    private val EventsApi: EventsApi,
    private val catId: String
) : PagingSource<Int, ProductModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductModel> {
        val page = params.key ?: PAGING_START_INDEX

        return try {
            val result = EventsApi.allCategorizationByUserId(
                page.toString(),
                PAGING_PER_PAGE.toString(),
                catId,
                UserUtil.getUserId()
            ).body()!!.allCatDtos.map { it.toAllCatsModel() }[0].allProducts

            LoadResult.Page(
                data = result,
                prevKey = if (page == PAGING_START_INDEX) null else page - 1,
                nextKey = if (result.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}