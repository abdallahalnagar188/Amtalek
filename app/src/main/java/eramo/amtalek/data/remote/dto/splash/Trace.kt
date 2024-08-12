package eramo.amtalek.data.remote.dto.splash


import com.google.gson.annotations.SerializedName

data class Trace(
    @SerializedName("class")
    var classX: String?,
    @SerializedName("file")
    var `file`: String?,
    @SerializedName("function")
    var function: String?,
    @SerializedName("line")
    var line: Int?,
    @SerializedName("type")
    var type: String?
)