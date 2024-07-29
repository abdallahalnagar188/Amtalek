package eramo.amtalek.data.remote.dto.myHome.allCitys


import com.google.gson.annotations.SerializedName

data class AllCityResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)