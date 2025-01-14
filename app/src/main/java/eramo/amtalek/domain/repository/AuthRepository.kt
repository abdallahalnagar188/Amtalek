package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.bases.GeneralLoginResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthRepository {

    suspend fun onBoardingScreens(): Flow<Resource<List<OnBoardingModel>>>

    suspend fun register(
        firstName: RequestBody?,
        lastName: RequestBody?,
        phone: RequestBody?,
        email: RequestBody?,
        password: RequestBody?,
        confirmPassword: RequestBody?,
        gender: RequestBody?,
//        countryId: RequestBody?,
//        cityId: RequestBody?,
        regionId:RequestBody?,
        birthday:RequestBody?,
        companyName:RequestBody?,
        iam:RequestBody?,
        companyLogo: MultipartBody.Part?
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
    ): Flow<Resource<GeneralLoginResponse>>

    suspend fun logout(): Flow<Resource<ResultModel>>

    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<ResultModel>>

    suspend fun suspendAccount(): Flow<Resource<ResultModel>>

    suspend fun getCountries(): Flow<Resource<List<CountryModel>>>

    suspend fun getCities(countryId: String): Flow<Resource<List<CityModel>>>
    suspend fun getRegions(cityId: String): Flow<Resource<List<RegionModel>>>
    suspend fun getSubRegions(regionId: String): Flow<Resource<List<RegionModel>>>

    suspend fun getContactUsInfo(): Flow<Resource<ContactUsInfoModel>>

    suspend fun sendContactUsMessage(
        name: String,
        mobileNumber: String,
        email: String,
        message: String
    ): Flow<Resource<ResultModel>>

}