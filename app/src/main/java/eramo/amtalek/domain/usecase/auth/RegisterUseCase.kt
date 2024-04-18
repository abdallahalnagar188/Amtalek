package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun register(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String,
        gender: String,
        countryId: String,
        cityId: String,
        regionId:String,
        companyName: String?,
        iam:String,
        companyLogo: MultipartBody.Part?
    ): Flow<Resource<ResultModel>> {
        val isBlank = firstName.isBlank() || lastName.isBlank()
                || phone.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || gender.isBlank() || countryId.isBlank() || cityId.isBlank()

        return if (isBlank) flow { }
        else repository.register(
            firstName, lastName, phone, email, password, confirmPassword, gender, countryId, cityId, regionId,companyName, iam, companyLogo
            )
    }

    suspend fun sendVerificationCodeEmail(
        email: String
    ): Flow<Resource<ResultModel>> {
        return repository.sendVerificationCodeEmail(email)

    }
}