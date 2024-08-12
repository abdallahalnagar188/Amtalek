package eramo.amtalek.data.remote.dto.splash


import com.google.gson.annotations.SerializedName

data class SplashResponse(
    @SerializedName("exception")
    var exception: String?,
    @SerializedName("file")
    var `file`: String?,
    @SerializedName("line")
    var line: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("trace")
    var trace: List<Trace?>?
)