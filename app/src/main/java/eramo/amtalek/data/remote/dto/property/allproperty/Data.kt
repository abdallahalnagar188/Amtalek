package eramo.amtalek.data.remote.dto.property.allproperty


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("exception")
    var exception: Any?,
    @SerializedName("headers")
    var headers: Headers?,
    @SerializedName("original")
    var original: Original?
)