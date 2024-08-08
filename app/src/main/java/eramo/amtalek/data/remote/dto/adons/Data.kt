package eramo.amtalek.data.remote.dto.adons


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("monthly_price")
    var monthlyPrice: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("quantity")
    var quantity: Int?,
    @SerializedName("yearly_price")
    var yearlyPrice: String?
)