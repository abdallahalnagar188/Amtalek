package eramo.amtalek.domain.search

import android.os.Parcelable
import androidx.privacysandbox.ads.adservices.adid.AdId
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchModelDto(
    val searchKeyWords:String?,
    var locationId:String?,
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
    val city:Int? = 0 ,
    var amenitiesListIds:String?,
    var priceArrangeKeys:String?,

) : Parcelable
