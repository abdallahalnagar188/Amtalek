package eramo.amtalek.data.remote.dto.property.newResponse.property_purpose


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.property.CriteriaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyPurposeResponse(
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
    @SerializedName("properties_count")
    var propertiesCount: Int?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toCriteriaModel(): CriteriaModel {
        return CriteriaModel(id = id?:0, title = title?:"", propertyCount = propertiesCount?:0)
    }
}