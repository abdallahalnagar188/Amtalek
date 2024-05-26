package eramo.amtalek.data.remote.dto.fav


import com.google.gson.annotations.SerializedName

data class AddOrRemoveFavResponse(
    @SerializedName("data")
    var `data`: List<Any?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)