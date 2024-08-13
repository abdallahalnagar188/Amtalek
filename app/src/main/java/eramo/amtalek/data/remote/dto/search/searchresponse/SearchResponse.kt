package eramo.amtalek.data.remote.dto.search.searchresponse


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable
@Parcelize
data class Data(
    @SerializedName("data")
    var `data`: List<DataX>?,
    @SerializedName("featured_count")
    var featuredCount: Int?,
    @SerializedName("links")
    var links: Links?,
    @SerializedName("meta")
    var meta: Meta?,
    @SerializedName("normal_count")
    var normalCount: Int?,
    @SerializedName("total_props")
    var totalProps: Int?
) : Parcelable{

}
@Parcelize
data class DataX(
    @SerializedName("acceptance")
    var acceptance: String?,
    @SerializedName("address")
    var address: String?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("broker_details")
    var brokerDetails: BrokerDetails?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("sub_region")
    var subRegion: String?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toPropertyModel(): PropertyModel {
        return PropertyModel(
            id = id?:0,
            imageUrl = primaryImage?:"",
            title = title?:"",
            type = forWhat?:"",
            isFavourite = isFav?:"",
            rentPrice = rentPrice?:0,
            sellPrice = salePrice?:0,
            bedsCount = bedRoomsNo?:0,
            bathroomsCount = bathRoomNo?:0,
            area = landArea?:0,
            brokerId = (brokerDetails?.id?:0).toString(),
            brokerLogoUrl = brokerDetails?.companyLogo?:"",
            location = "$city, $region",
            datePosted = createdAt?:"",
            isFeatured = priority?:"",
            rentDuration = rentDuration?:"",
            listingNumber = listingNumber?:"",
            currency = currency?:"",
            acceptance = acceptance?:"",
            sold = sold?:false,
            offerData = null,
            region = region?:"",
            subRegion = subRegion?:"",
            vendorType = brokerDetails?.brokerType?:"",
            senderName = brokerDetails?.companyName?:"",
            senderPhone = "",
            offerPrice = "",
            offerDate = "",
        )
    }

}

@Parcelize
data class Meta(
    @SerializedName("current_page")
    var currentPage: Int?,
    @SerializedName("from")
    var from: Int?,
    @SerializedName("last_page")
    var lastPage: Int?,
    @SerializedName("links")
    var links: List<Link?>?,
    @SerializedName("path")
    var path: String?,
    @SerializedName("per_page")
    var perPage: Int?,
    @SerializedName("to")
    var to: Int?,
    @SerializedName("total")
    var total: Int?
) : Parcelable
@Parcelize
data class Links(
    @SerializedName("first")
    var first: String?,
    @SerializedName("last")
    var last: String?,
    @SerializedName("next")
    var next: String?,
    @SerializedName("prev")
    var prev: String?
) : Parcelable
@Parcelize
data class Link(
    @SerializedName("active")
    var active: Boolean?,
    @SerializedName("label")
    var label: String?,
    @SerializedName("url")
    var url: String?
) : Parcelable
@Parcelize
data class BrokerDetails(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("company_logo")
    var companyLogo: String?,
    @SerializedName("company_name")
    var companyName: String?,
    @SerializedName("id")
    var id: Int?
) : Parcelable
object GlobalProperties {
      var totalItems: String =""
}
// Method to handle paginated response
