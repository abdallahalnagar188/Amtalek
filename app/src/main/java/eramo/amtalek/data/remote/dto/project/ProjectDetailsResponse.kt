package eramo.amtalek.data.remote.dto.project


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.project.AmenityModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectDetailsResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
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
    var twitter: String?
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("aminities")
    var aminities: List<Aminity?>?,
    @SerializedName("autocad")
    var autocad: List<Autocad?>?,
    @SerializedName("broker_details")
    var brokerDetails: List<BrokerDetail?>?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("currency")
    var currency: String?,
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
    @SerializedName("location")
    var location: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("quick_summary")
    var quickSummary: QuickSummary?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("sliders")
    var sliders: List<Slider?>?,
    @SerializedName("start_price")
    var startPrice: String?,
    @SerializedName("twitter")
    var twitter: String?,
    @SerializedName("video")
    var video: String?,
    @SerializedName("video_type")
    var videoType: String?
) : Parcelable{

}

@Parcelize
data class BrokerDetail(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("projects")
    var projects: List<Project?>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
) : Parcelable

@Parcelize
data class Autocad(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("src")
    var src: String?
) : Parcelable

@Parcelize
data class QuickSummary(
    @SerializedName("address")
    var address: String?,
    @SerializedName("bedrooms")
    var bedrooms: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("delivery_time")
    var deliveryTime: String?,
    @SerializedName("start_bedrooms")
    var startBedrooms: String?,
    @SerializedName("Start_Price")
    var startPrice: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("total_units")
    var totalUnits: String?
) : Parcelable

@Parcelize
data class Slider(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("src")
    var src: String?
) : Parcelable

@Parcelize
data class Aminity(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toAmenityModel(): AmenityModel{
        return AmenityModel(id?:0, title?:"")
    }
}

@Parcelize
data class AgentData(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
) : Parcelable