package eramo.amtalek.data.remote.dto.property.newResponse.amenities


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.project.AmenityModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmenitiesResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toAmenityModel():AmenityModel{
        return AmenityModel(id = id?:-1, name = title?:"")
    }
}