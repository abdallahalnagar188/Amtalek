package eramo.amtalek.domain.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchModelDto(
    val searchKeyWords:String?,
    val locationId:Int?,
    val propertyTypeId:Int?,
    val currencyId:Int?,
    val propertyFinishingId:Int?,
    val purposeId:Int?,
    val bathroomsNumber:Int?,
    val bedroomsNumber:Int?,
    val minPrice:Int?,
    val maxPrice:Int?,
    val minArea:Int?,
    val maxArea:Int?,
    val amenitiesListIds:List<Int>?
) : Parcelable
