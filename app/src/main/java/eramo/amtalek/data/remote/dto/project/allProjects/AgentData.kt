package eramo.amtalek.data.remote.dto.project.allProjects


import com.google.gson.annotations.SerializedName

data class AgentData(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("has_package")
    var hasPackage: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("projects")
    var projects: List<Project>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("property_for_rent")
    var propertyForRent: Int?,
    @SerializedName("property_for_sale")
    var propertyForSale: Int?
)