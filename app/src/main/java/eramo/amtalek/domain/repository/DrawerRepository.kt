package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.GetProfileResponse
import eramo.amtalek.data.remote.dto.editprofile.EditProfileResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.drawer.PolicyInfoModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DrawerRepository {

    suspend fun updateFirebaseDeviceToken(deviceToken: String): Flow<Resource<ResultModel>>

    suspend fun getProfile(type:String,id:String): Flow<Resource<GetProfileResponse>>

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

    suspend fun updateProfilePics(
        image_key: RequestBody?,
        image: MultipartBody.Part?
    ): Flow<Resource<EditProfileResponse>>

    suspend fun editProfile(
        firstName: String?,
        lastName: String?,
        phone: String?,
        email: String?,
        countryId: String?,
        cityId: String?,
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