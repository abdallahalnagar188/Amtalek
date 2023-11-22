package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.API_OPERATION_TYPE_FORGET_PASSWORD
import eramo.amtalek.util.API_OPERATION_TYPE_VERIFY_CODE
import eramo.amtalek.util.API_SUSPEND_CODE
import eramo.amtalek.util.FROM_ANDROID
import eramo.amtalek.util.NOT_ROBOT
import eramo.amtalek.util.SIGN_UP_GENDER_ACCEPT_CONDITION
import eramo.amtalek.util.SIGN_UP_GENDER_ACCEPT_NOT_ROBOT
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val amtalekApi: AmtalekApi) : AuthRepository {

    override suspend fun onBoardingScreens(): Flow<Resource<List<OnBoardingModel>>> {
        return flow {
            val result = toResultFlow { amtalekApi.onBoardingScreens() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allScreens?.map { it.toOnBoardingModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String,
        gender: String,
        countryId: String,
        cityId: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.register(
                    firstName, lastName, phone, email, password, confirmPassword, gender, countryId, cityId,
                    FROM_ANDROID,
                    SIGN_UP_GENDER_ACCEPT_CONDITION,
                    SIGN_UP_GENDER_ACCEPT_NOT_ROBOT
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun sendVerificationCodeEmail(email: String): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendOtpCodeEmail(email, API_OPERATION_TYPE_VERIFY_CODE)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun sendForgetPasswordCodeEmail(email: String): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendOtpCodeEmail(email, API_OPERATION_TYPE_FORGET_PASSWORD)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun changePasswordForgetPassword(
        email: String,
        code: String,
        newPassword: String,
        rePassword: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.changePasswordForgetPassword(email, code, newPassword, rePassword)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun checkOtpCode(
        email: String,
        otpCode: String,
        operationType: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.checkOtpCode(email, otpCode, operationType)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String,
        firebaseToken: String
    ): Flow<Resource<UserModel>> {
        return flow {
            val result = toResultFlow { amtalekApi.login(email, password, firebaseToken) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.data?.toUserModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun logout(): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow { amtalekApi.logout(UserUtil.getUserToken()) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.updatePassword(
                    UserUtil.getUserToken(),
                    currentPassword, newPassword, confirmPassword
                )

            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun suspendAccount(): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.suspendAccount(UserUtil.getUserToken(), API_SUSPEND_CODE)
            }

            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun getCountries(): Flow<Resource<List<CountryModel>>> {
        return flow {
            val result = toResultFlow { amtalekApi.getCountries() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.data?.map { it!!.toCountryModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun getCities(countryId: String): Flow<Resource<List<CityModel>>> {
        return flow {
            val result = toResultFlow { amtalekApi.getCities(countryId) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.data?.map { it!!.toCityModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun getContactUsInfo(): Flow<Resource<ContactUsInfoModel>> {
        return flow {
            val result = toResultFlow { amtalekApi.contactUsInfo() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val data = apiState.data?.toContactUsInfoModel()
                        emit(Resource.Success(data))
                    }
                }
            }
        }
    }


    override suspend fun sendContactUsMessage(
        name: String,
        mobileNumber: String,
        email: String,
        message: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendContactUsMessage(
                    name, mobileNumber, email, message, FROM_ANDROID, NOT_ROBOT
                )
            }

            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }
}