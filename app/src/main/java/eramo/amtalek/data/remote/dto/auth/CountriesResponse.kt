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
        val title: String?
    ) {
        fun toCountryModel(): CountryModel {
            return CountryModel(
                id ?: -1,
                title ?: "",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/220px-Flag_of_Egypt.svg.png"
            )
        }
    }

}