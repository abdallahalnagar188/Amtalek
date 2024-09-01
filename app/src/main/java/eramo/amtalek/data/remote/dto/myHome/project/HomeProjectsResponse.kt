package eramo.amtalek.data.remote.dto.myHome.project


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.home.project.HomeProjectsSectionModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeProjectsResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class MyData(
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
    @SerializedName("sub_region")
    var subRegion: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("total_units")
    var totalUnits: String?,
    @SerializedName("twitter")
    var twitter: String?
) : Parcelable{
    fun toProjectModel():ProjectModel{
        return ProjectModel(
            id = id?:-1,
            brokerId = agentData?.id?:-1,
            brokerLogoUrl = agentData?.companyLogo?:"",
            datePosted = createdAt?:"",
            description = description?:"",
            imageUrl = image?:"",
            isFavourite = "--",
            listingNumber = listingNumber?:"",
            title = title?:"",
            isFeatured = "------",
            location = "$city, $region, $subRegion",
            priceFrom = priceFrom?:""


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
    fun toHomeProjectsSectionModel(): HomeProjectsSectionModel {
        return HomeProjectsSectionModel(
            title = title?:"",
            projects = data?.map { it.toProjectModel() }?: emptyList()
        )
    }
}

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