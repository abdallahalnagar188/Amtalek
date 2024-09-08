package eramo.amtalek.data.remote.dto.myHome.news.newsDetails


import com.google.gson.annotations.SerializedName

data class AgentInfo(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: Any?,
    @SerializedName("has_package")
    var hasPackage: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: Any?,
    @SerializedName("name")
    var name: Any?,
    @SerializedName("phone")
    var phone: Any?,
    @SerializedName("projects")
    var projects: List<Any?>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("property_for_rent")
    var propertyForRent: Int?,
    @SerializedName("property_for_sale")
    var propertyForSale: Int?
)