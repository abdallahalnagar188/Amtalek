package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun register(
        firstName: RequestBody?,
        lastName: RequestBody?,
        phone: RequestBody?,
        email: RequestBody?,
        password: RequestBody?,
        birthday:RequestBody?,
        confirmPassword: RequestBody?,
        gender: RequestBody?,
//        countryId: RequestBody?,
//        cityId: RequestBody?,
        regionId:RequestBody?,
        companyName: RequestBody?,
        iam:RequestBody?,
        companyLogo: MultipartBody.Part?
    ): Flow<Resource<ResultModel>> {
     return repository.register(
            firstName = firstName, lastName = lastName, phone = phone, email =  email, password =  password, birthday =  birthday, confirmPassword = confirmPassword,
        gender =  gender,
//         countryId =  countryId, cityId = cityId,

         regionId = regionId, companyName = companyName,
         iam = iam, companyLogo =  companyLogo
            )
    }

    suspend fun sendVerificationCodeEmail(
        email: String
    ): Flow<Resource<ResultModel>> {
        return repository.sendVerificationCodeEmail(email)

    }
}