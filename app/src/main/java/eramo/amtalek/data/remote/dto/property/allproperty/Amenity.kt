package eramo.amtalek.data.remote.dto.property.allproperty


import com.google.gson.annotations.SerializedName

data class Amenity(
    @SerializedName("amenities")
    var amenities: Int?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("property_id")
    var propertyId: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?
)