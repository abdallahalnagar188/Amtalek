package eramo.amtalek.data.remote.dto.search.searchform


import com.google.gson.annotations.SerializedName

data class SearchFilterationResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)