package eramo.amtalek.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.util.PAGING_PER_PAGE
import eramo.amtalek.util.PAGING_START_INDEX
import eramo.amtalek.util.TEXT_NO
import eramo.amtalek.util.UserUtil
import retrofit2.HttpException
import java.io.IOException

class PagingProducts(private val AmtalekApi: AmtalekApi) :
    PagingSource<Int, ProductModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductModel> {
        val page = params.key ?: PAGING_START_INDEX

        return try {
            val result = AmtalekApi.allProductsByUserId(
                page.toString(),
                PAGING_PER_PAGE.toString(),
                UserUtil.getUserId(),
                TEXT_NO
            ).body()!!.allProducts.map { it.toProductModel() }

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