package eramo.amtalek.data.remote.dto.myHome.mostviewd


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.home.property.HomePropertySectionModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeMostViewsResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class MyData(
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
    @SerializedName("normal_featured")
    var normalFeatured: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
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
            location = city?:"",
            datePosted = createdAt?:"",
            isFeatured = ""?:"",
            rentDuration = rentDuration?:"",
            listingNumber = listingNumber?:"",
        )
    }

}

@Parcelize
data class Data(
    @SerializedName("data")
    var `data`: List<MyData>?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toHomePropertySectionModel(): HomePropertySectionModel {
        return HomePropertySectionModel(
            title = title?:"",
            propertiesList = data?.map { it.toPropertyModel() }?: emptyList()
        )
    }

}

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