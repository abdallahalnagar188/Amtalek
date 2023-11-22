package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.SuccessfulResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthRepository {

    suspend fun onBoardingScreens(): Flow<Resource<List<OnBoardingModel>>>

    suspend fun register(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String,
        gender: String,
        countryId: String,
        cityId: String
    ): Flow<Resource<ResultModel>>

    suspend fun sendVerificationCodeEmail(
        email: String
    ): Flow<Resource<ResultModel>>

    suspend fun sendForgetPasswordCodeEmail(
        email: String
    ): Flow<Resource<ResultModel>>

    suspend fun changePasswordForgetPassword(
        email: String,
        code: String,
        newPassword: String,
        rePassword: String
    ): Flow<Resource<ResultModel>>

    suspend fun checkOtpCode(
        email: String,
        otpCode: String,
        operationType: String
    ): Flow<Resource<ResultModel>>

    suspend fun login(
        email: String,
        password: String,
        firebaseToken: String
    ): Flow<Resource<UserModel>>

    suspend fun logout(): Flow<Resource<ResultModel>>

    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<ResultModel>>

    suspend fun suspendAccount(): Flow<Resource<ResultModel>>

    suspend fun getCountries(): Flow<Resource<List<CountryModel>>>

    suspend fun getCities(countryId: String): Flow<Resource<List<CityModel>>>

    suspend fun getContactUsInfo(): Flow<Resource<ContactUsInfoModel>>

    suspend fun sendContactUsMessage(
        name: String,
        mobileNumber: String,
        email: String,
        message: String
    ): Flow<Resource<ResultModel>>

}