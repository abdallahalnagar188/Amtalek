package eramo.amtalek.data.remote.dto.myHome.sliders


import com.google.gson.annotations.SerializedName

data class HomeSlidersResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)
data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?
)