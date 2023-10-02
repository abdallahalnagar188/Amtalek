package eramo.amtalek.data.remote.dto.drawer.myaccount

import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.general.Member

data class EditProfileResponse (
    @SerializedName("success" ) var success : Int?    = null,
    @SerializedName("member"  ) var member  : Member? = Member(),
    @SerializedName("message" ) var message : String? = null
)