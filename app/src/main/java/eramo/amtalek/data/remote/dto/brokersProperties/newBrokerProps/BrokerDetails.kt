package eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps


import com.google.gson.annotations.SerializedName

data class BrokerDetails(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("company_logo")
    var companyLogo: String?,
    @SerializedName("company_name")
    var companyName: String?,
    @SerializedName("id")
    var id: Int?
)