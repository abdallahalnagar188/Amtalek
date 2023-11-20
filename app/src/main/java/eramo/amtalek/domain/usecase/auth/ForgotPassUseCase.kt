package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.API_OPERATION_TYPE_FORGET_PASSWORD
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForgotPassUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun sendForgetPasswordCodeEmail(email: String): Flow<Resource<ResultModel>> {
        return repository.sendForgetPasswordCodeEmail(email)
    }

    suspend fun checkOtpCode(
        email: String,
        otpCode: String,
    ): Flow<Resource<ResultModel>> {
        return repository.checkOtpCode(email, otpCode, API_OPERATION_TYPE_FORGET_PASSWORD)
    }

    suspend fun changePasswordForgetPassword(
        email: String,
        code: String,
        newPassword: String,
        rePassword: String
    ): Flow<Resource<ResultModel>> {
        return repository.changePasswordForgetPassword(email, code, newPassword, rePassword)
    }
}