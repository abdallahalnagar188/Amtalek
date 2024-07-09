package eramo.amtalek.data.remote.dto.search.alllocations


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.search.LocationModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllLocationsResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable{
    fun mapLocations(): List<LocationModel>{
        val locations = mutableListOf<LocationModel>()
        for (item in data!!){
            val location = LocationModel(
                id = item?.id?:-1,
                title = item?.title?:"",
                propertiesCount = item?.propertiesCount?:-1
            )
            locations.add(location)
        }
        return locations
    }
}

@Parcelize
data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?,
    @SerializedName("title")
    var title: String?
) : Parcelable{

}