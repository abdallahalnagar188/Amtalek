package eramo.amtalek.domain.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchModelDto(
    val searchKeyWords:String?,
    val locationId:String?,
    val locationName:String?,
    var propertyTypeId:String?,
    var currencyId:Int?,
    var propertyFinishingId:String?,
    var purposeId:String?,
    val bathroomsNumber:String?,
    val bedroomsNumber:String?,
    val minPrice:String?,
    val maxPrice:String?,
    val minArea:String?,
    val maxArea:String?,
    val amenitiesListIds:List<Int>?
) : Parcelable
