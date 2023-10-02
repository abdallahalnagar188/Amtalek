package eramo.amtalek.data.remote.dto.auth

import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.domain.model.auth.LoginModel

data class LoginDto(
    @SerializedName("member") var member: Member? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("success") var success: Int? = null,
    @SerializedName("message") var message: String? = null
) {
    fun toLoginModel(): LoginModel {
        return LoginModel(member, token, success, message)
    }
}