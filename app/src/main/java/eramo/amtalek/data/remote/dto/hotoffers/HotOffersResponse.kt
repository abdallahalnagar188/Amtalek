package eramo.amtalek.data.remote.dto.hotoffers


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotOffersResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable
@Parcelize
data class Project(
    @SerializedName("agent_data")
    var agentData: AgentData?,
    @SerializedName("bedrooms")
    var bedrooms: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("delivery_date")
    var deliveryDate: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facebook")
    var facebook: String?,
    @SerializedName("google_plus")
    var googlePlus: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("price_from")
    var priceFrom: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("total_units")
    var totalUnits: String?,
    @SerializedName("twitter")
    var twitter: String?,
    @SerializedName("sub_region")
    var subRegion: String?,

) : Parcelable{
    fun toProjectModel(): ProjectModel {
        return ProjectModel(
            id = id?:0,
            imageUrl = image?:"",
            title = title?:"",
            location = "$city, $region, $subRegion",
            datePosted = createdAt?:"",
            isFeatured = ""?:"",
            description = description?:"",
            brokerId = agentData?.id?:0,
            brokerLogoUrl = agentData?.companyLogo?:"",
            isFavourite = "",
            listingNumber = listingNumber?:"",
        )
    }
}
@Parcelize
data class Property(
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
    @SerializedName("priority")
    var priority: String?,
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
    var title: String?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("acceptance")
    var acceptance: String?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("sub_region")
    var subRegion: String?,
) : Parcelable{
    fun toPropertyModel():PropertyModel{
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
            location = "$city, $region, $subRegion",
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

@Parcelize
data class Data(
    @SerializedName("projects")
    var projects: List<Project?>?,
    @SerializedName("properties")
    var properties: List<Property?>?
) : Parcelable

@Parcelize
data class AgentData(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("company_logo")
    var companyLogo: String?,
    @SerializedName("company_name")
    var companyName: String?,
    @SerializedName("id")
    var id: Int?
) : Parcelable