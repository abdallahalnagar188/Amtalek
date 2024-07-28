package eramo.amtalek.data.remote.dto.project.allProjects


import com.google.gson.annotations.SerializedName

data class AllProjectsResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)