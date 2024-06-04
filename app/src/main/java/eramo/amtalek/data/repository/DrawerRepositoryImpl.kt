package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.GetProfileResponse
import eramo.amtalek.data.remote.dto.editprofile.EditProfileResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.drawer.PolicyInfoModel
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiText
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DrawerRepositoryImpl(private val AmtalekApi: AmtalekApi) : DrawerRepository {


    override suspend fun getProfile(type:String,id:String): Flow<Resource<GetProfileResponse>> {
        return flow {
            val result = toResultFlow { AmtalekApi.getProfile(UserUtil.getUserToken(),type, id) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val model = apiState.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun updateProfile(
        firstName: RequestBody?,
        lastName: RequestBody?,
        mobileNumber: RequestBody?,
        email: RequestBody?,
        countryId: RequestBody?,
        cityId: RequestBody?,
        bio: RequestBody?,
        profileImage: MultipartBody.Part?,
        coverImage: MultipartBody.Part?
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                AmtalekApi.updateProfile(
                    UserUtil.getUserToken(),
                    firstName, lastName, mobileNumber, email, countryId, cityId, bio, profileImage, coverImage
                )
            }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val model = apiState.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun updateProfilePics(image_key: RequestBody?, image: MultipartBody.Part?
    ): Flow<Resource<EditProfileResponse>> {
       return flow {
           val result = toResultFlow {
               AmtalekApi.updateProfilePics(
                   UserUtil.getUserToken(),
                   image_key,
                   image
               )
           }
           result.collect { apiState ->
               when (apiState) {
                   is ApiState.Loading -> emit(Resource.Loading())
                   is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                   is ApiState.Success -> {
                       val model = apiState.data
                       emit(Resource.Success(model))
                   }
               }
           }

       }
    }

    override suspend fun updateFirebaseDeviceToken(deviceToken: String): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                AmtalekApi.updateFirebaseDeviceToken(
                    UserUtil.getUserId(),
                    deviceToken
                )
            }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        apiState.data?.let {
                            emit(Resource.Success(it.toResultModel()))
                        } ?: emit(Resource.Error(UiText.DynamicString("Empty member")))
                    }
                }
            }
        }
    }

    override suspend fun editProfile(
        firstName: String?,
        lastName: String?,
        phone: String?,
        email: String?,
        countryId: String?,
        cityId: String?,
    ): Flow<Resource<EditProfileResponse>> {
        return flow {
            val result = toResultFlow {
                AmtalekApi.editProfile(
                    userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    email = email,
                    countryId = countryId,
                    cityId = cityId
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> emit(Resource.Success(it.data))
                }
            }
        }
    }

    override suspend fun getAppInfo(): Flow<Resource<AppInfoResponse>> {
        return flow {
            val result = toResultFlow { AmtalekApi.getAppInfo() }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> emit(Resource.Success(it.data))
                }
            }
        }
    }

    override suspend fun getAppPolicy(): Flow<Resource<List<PolicyInfoModel>>> {
        return flow {
            val result = toResultFlow { AmtalekApi.getAppPolicy() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.policyInfoDto?.map { it.toPolicyInfoModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun contactMsg(
        user_name: String,
        user_email: String,
        user_phone: String,
        subject: String,
        details: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                AmtalekApi.contactMsg(
                    UserUtil.getUserId(),
                    user_name,
                    user_email,
                    user_phone,
                    subject,
                    details
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> emit(Resource.Success(it.data?.toResultModel()))
                }
            }
        }
    }
}