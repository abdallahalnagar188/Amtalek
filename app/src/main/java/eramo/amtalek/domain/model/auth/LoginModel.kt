package eramo.amtalek.domain.model.auth

import eramo.amtalek.data.remote.dto.general.Member

data class LoginModel(
    var member: Member? = null,
    var token: String? = null,
    var success: Int? = null,
    var message: String? = null
)