package eramo.amtalek.data.remote.dto.contactBrokerDetails

import com.google.gson.annotations.SerializedName


data class ContactUsResponseInProperty(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: List<Any>?
)