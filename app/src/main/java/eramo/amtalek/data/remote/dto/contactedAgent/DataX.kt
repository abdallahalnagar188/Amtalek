package eramo.amtalek.data.remote.dto.contactedAgent


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("actor_type")
    var actorType: String?,
    @SerializedName("contacting_status")
    var contactingStatus: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("property_id")
    var propertyId: Any?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("user_id")
    var userId: Int?,
    @SerializedName("vendor_id")
    var vendorId: String?
)