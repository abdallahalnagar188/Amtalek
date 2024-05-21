package eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment


import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.property.newResponse.prop_details.Data

data class SendPropertyCommentResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)
data class Data(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("property_id")
    var propertyId: String?,
    @SerializedName("starts")
    var starts: String?,
    @SerializedName("updated_at")
    var updatedAt: String?
)