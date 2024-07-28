package eramo.amtalek.data.remote.dto.property.allproperty


import com.google.gson.annotations.SerializedName

data class Original(
    @SerializedName("data")
    var `data`: List<DataX>?,
    @SerializedName("links")
    var links: Links?,
    @SerializedName("meta")
    var meta: Meta?
)