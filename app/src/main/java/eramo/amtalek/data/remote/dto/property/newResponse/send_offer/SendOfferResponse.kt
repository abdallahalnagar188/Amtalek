package eramo.amtalek.data.remote.dto.property.newResponse.send_offer


import com.google.gson.annotations.SerializedName

data class SendOfferResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
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
    @SerializedName("name")
    var name: String?,
    @SerializedName("offer")
    var offer: String?,
    @SerializedName("offer_type")
    var offerType: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("property_id")
    var propertyId: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?
)