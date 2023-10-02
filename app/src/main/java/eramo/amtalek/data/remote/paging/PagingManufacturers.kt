package eramo.amtalek.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.domain.model.products.CategoryModel
import eramo.amtalek.util.Constants
import eramo.amtalek.util.UserUtil
import retrofit2.HttpException
import java.io.IOException

class PagingManufacturers(private val EventsApi: EventsApi) : PagingSource<Int, CategoryModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryModel> {
        val page = params.key ?: Constants.PAGING_START_INDEX

        return try {
            val result =
                EventsApi.allProductsManufacturersByUserId(
                    page.toString(),
                    Constants.PAGING_PER_PAGE.toString(),
                    UserUtil.getUserId()
                ).body()!!.all_cats.map { it.toCategoryModel() }

            LoadResult.Page(
                data = result,
                prevKey = if (page == Constants.PAGING_START_INDEX) null else page - 1,
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

    override fun getRefreshKey(state: PagingState<Int, CategoryModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}