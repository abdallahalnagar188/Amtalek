package eramo.amtalek.data.remote.dto.property.allproperty


import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("active")
    var active: Boolean?,
    @SerializedName("label")
    var label: String?,
    @SerializedName("url")
    var url: String?
)