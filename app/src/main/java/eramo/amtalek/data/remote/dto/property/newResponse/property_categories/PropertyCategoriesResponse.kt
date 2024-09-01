package eramo.amtalek.data.remote.dto.property.newResponse.property_categories


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.property.CriteriaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyCategoriesResponse(
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
    @SerializedName("name")
    var name: String?
) : Parcelable{
    fun toCriteriaModel(): CriteriaModel {
        return CriteriaModel(id = id?:0, title = name?:"", propertyCount = 0, image = "")
    }
}