package eramo.amtalek.data.remote.dto.project.allProjects


import com.google.gson.annotations.SerializedName

data class AgentDataX(
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
)