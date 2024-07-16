package eramo.amtalek.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.search.searchresponse.GlobalProperties
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.util.PAGING_START_INDEX
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.itemsCount
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException


class PagingSearch(
    private val amtalekApi: AmtalekApi,
    private val keyword: RequestBody?,
    private val city: RequestBody?,
    private val country: RequestBody?,
    private val currency: RequestBody?,
    private val finishing: RequestBody?,
    private val minArea: RequestBody?,
    private val maxArea: RequestBody?,
    private val minPrice: RequestBody?,
    private val maxPrice: RequestBody?,
    private val minBathes: RequestBody?,
    private val minBeds: RequestBody?,
    private val purpose: RequestBody?,
    private val region: RequestBody?,
    private val subRegion: RequestBody?,
    private val propertyType: RequestBody?,
    private val priceArrangeKeys: RequestBody?,
    private val amenities: RequestBody?,
) : PagingSource<Int, PropertyModel>() {
    override fun getRefreshKey(state: PagingState<Int, PropertyModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PropertyModel> {
        val page = params.key ?: PAGING_START_INDEX
        return try {
            val response = amtalekApi.search(
                userToken = if (UserUtil.isUserLogin()) "Bearer ${UserUtil.getUserToken()}" else null,
                keyword = keyword,
                city = city,
                country = country,
                currency = currency,
                finishing = finishing,
                minArea = minArea,
                maxArea = maxArea,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minBathes = minBathes,
                minBeds = minBeds,
                purpose = purpose,
                region = region,
                subRegion = subRegion,
                page = page,
                propertyType = propertyType,
                amenities = amenities,
                priceArrangeKeys = priceArrangeKeys
            ).body()
            // Check if response or data is null
            val responseData = response?.data?.getOrNull(0)
            if (responseData == null) {
                itemsCount.value ="0"
            }else{
                GlobalProperties.totalItems = responseData.meta?.total.toString()
                itemsCount.value = responseData.meta?.total.toString()
            }

            val result = response?.data?.get(0)?.data?.map { it.toPropertyModel() } ?: emptyList()

            LoadResult.Page(
                data = result,
                prevKey = if (page == PAGING_START_INDEX) null else page - 1,
                nextKey = if (result.isEmpty()) null else page + 1
            )

        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}