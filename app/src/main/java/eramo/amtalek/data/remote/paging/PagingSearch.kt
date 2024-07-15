package eramo.amtalek.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.search.SearchResponseModel
import eramo.amtalek.util.PAGING_START_INDEX
import eramo.amtalek.util.UserUtil
import retrofit2.HttpException
import java.io.IOException

class PagingSearch (
    private val amtalekApi: AmtalekApi,
    private val keyword:String?,
    private val city:String?,
    private val country:String?,
    private val currency:Int?,
    private val finishing:String?,
    private val minArea:String?,
    private val maxArea:String?,
    private val minPrice:String?,
    private val maxPrice:String?,
    private val minBathes:String?,
    private val minBeds:String?,
    private val purpose:String?,
    private val region:String?,
    private val subRegion:String?,
    private val propertyType:String?,
    private val priceArrangeKeys:String?,
    private val amenities:String?,
):PagingSource<Int,PropertyModel> (){
    override fun getRefreshKey(state: PagingState<Int, PropertyModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PropertyModel> {
        val page = params.key ?: PAGING_START_INDEX
        return try {
            val result = amtalekApi.search(
                userToken = if (UserUtil.isUserLogin()) "Bearer ${UserUtil.getUserToken()}" else null,
                keyword =keyword ,
                city = city,
                country = country,
                currency = currency,
                finishing = finishing,
                minArea = minArea,
                maxArea = maxArea,
                minPrice =minPrice ,
                maxPrice =maxPrice,
                minBathes = minBathes,
                minBeds = minBeds,
                purpose = purpose,
                region =region,
                subRegion = subRegion,
                page = page,
                propertyType = propertyType,
                amenities = amenities,
                priceArrangeKeys =priceArrangeKeys).body()!!.data.get(0).data!!.map {it.toPropertyModel()}
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