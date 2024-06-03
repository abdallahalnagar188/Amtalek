package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.CountryModel

data class CountriesResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("image")
        val image: String?,

    ) {
        fun toCountryModel(): CountryModel {
            return CountryModel(
                id = id ?: -1,
                name = title ?: "",
                imageUrl = image?:"",
            )
        }
    }

}