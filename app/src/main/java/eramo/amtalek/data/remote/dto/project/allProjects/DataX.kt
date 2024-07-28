package eramo.amtalek.data.remote.dto.project.allProjects


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("agent_data")
    var agentData: List<AgentData>?,
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
)