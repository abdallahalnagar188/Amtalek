package eramo.amtalek.data.remote.dto.userDetials.response


import com.google.gson.annotations.SerializedName

data class Offer(
    @SerializedName("convert_to_lead")
    var convertToLead: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("offer")
    var offer: String?,
    @SerializedName("offer_type")
    var offerType: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("property")
    var `property`: Property?,
    @SerializedName("property_id")
    var propertyId: Int?,
    @SerializedName("seen")
    var seen: String?,
    @SerializedName("updated_at")
    var updatedAt: String?
)