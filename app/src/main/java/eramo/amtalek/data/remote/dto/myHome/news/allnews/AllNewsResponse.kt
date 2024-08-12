package eramo.amtalek.data.remote.dto.myHome.news.allnews


import com.google.gson.annotations.SerializedName

data class AllNewsResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)