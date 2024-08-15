package eramo.amtalek.data.remote.dto.splash.splashV2


import com.google.gson.annotations.SerializedName

data class OnBordingResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) {
    data class Data(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("logo")
        var logo: String?,
        @SerializedName("text")
        var text: String?
    )
}