package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.EditProfileResponse
import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.model.drawer.PolicyInfoModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface DrawerRepository {

    suspend fun updateFirebaseDeviceToken(deviceToken: String): Flow<Resource<ResultModel>>

    suspend fun getProfile(): Flow<Resource<UserModel>>

    suspend fun updateProfile(
        firstName: RequestBody?,
        lastName: RequestBody?,
        mobileNumber: RequestBody?,
        email: RequestBody?,
        countryId: RequestBody?,
        cityId: RequestBody?,
        bio: RequestBody?,
        profileImage: MultipartBody.Part?,
        coverImage: MultipartBody.Part?
    ): Flow<Resource<ResultModel>>

    suspend fun editProfile(
        user_id: RequestBody?,
        user_pass: RequestBody?,
        user_name: RequestBody?,
        address: RequestBody?,
        countryId: RequestBody?,
        cityId: RequestBody?,
        regionId: RequestBody?,
        user_email: RequestBody?,
        user_phone: RequestBody?,
        m_image: MultipartBody.Part?
    ): Flow<Resource<EditProfileResponse>>

    suspend fun getAppInfo(): Flow<Resource<AppInfoResponse>>

    suspend fun getAppPolicy(): Flow<Resource<List<PolicyInfoModel>>>

    suspend fun contactMsg(
        user_name: String,
        user_email: String,
        user_phone: String,
        subject: String,
        details: String
    ): Flow<Resource<ResultModel>>
}