package eramo.amtalek.data.remote.dto.myHome.filter_by_city


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.home.cities.CitiesModel
import eramo.amtalek.domain.model.home.cities.HomeCitiesSectionsModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeCitiesResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("data")
    var `data`: List<MyData>?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toHomeCitiesSectionsModel():HomeCitiesSectionsModel {
        return HomeCitiesSectionsModel(
            title = title?:"",
            cities = data?.map { it.toCityModel() }
        )
    }
}

@Parcelize
data class MyData(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("properties_count")
    var propertiesCount: Int?,
    @SerializedName("rent_properties")
    var rentProperties: Int?,
    @SerializedName("sales_properties")
    var salesProperties: Int?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toCityModel(): CitiesModel {
        return CitiesModel(
            id = id?:0,
            title = title?:"",
            image = image?:"",
            propertiesCount = propertiesCount?:0,
            rentProperties = rentProperties?:0,
            salesProperties = salesProperties?:0,
            createdAt = createdAt?:""
        )

    }
}