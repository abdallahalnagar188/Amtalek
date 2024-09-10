package eramo.amtalek.data.remote.dto.userDetials.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("has_package")
    var hasPackage: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("offers")
    var offers: List<Offer?>?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("src")
    var src: String?,
    @SerializedName("submitted_props_for_rent")
    var submittedPropsForRent: List<Any?>?,
    @SerializedName("submitted_props_for_sale")
    var submittedPropsForSale: List<SubmittedPropsForSale?>?
)