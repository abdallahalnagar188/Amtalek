package eramo.amtalek.data.remote.dto


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.ResultModel

data class SuccessfulResponse(
    @SerializedName("data")
    val `data`: List<Any?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    fun toResultModel(): ResultModel {
        return ResultModel(status ?: -1, message ?: "")
    }
}