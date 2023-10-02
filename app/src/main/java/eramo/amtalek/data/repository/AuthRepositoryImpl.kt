package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.*
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.*
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val EventsApi: EventsApi) : AuthRepository {

    override suspend fun onBoardingScreens(): Flow<Resource<List<OnBoardingModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.onBoardingScreens() }
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
        user_name: String,
        user_email: String,
        user_phone: String,
        user_pass: String,
        address: String,
        countryId: String,
        cityId: String,
        regionId: String,
        gender: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                EventsApi.register(
                    user_name,
                    user_email,
                    user_phone,
                    user_pass,
                    address,
                    countryId,
                    cityId,
                    regionId,
                    gender
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(Resource.Error(UiText.DynamicString(model?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun loginApp(
        user_phone: String,
        user_pass: String
    ): Flow<Resource<LoginModel>> {
        return flow {
            val result = toResultFlow { EventsApi.loginApp(user_phone, user_pass) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        if (it.data?.success == 1) emit(Resource.Success(it.data.toLoginModel()))
                        else emit(Resource.Error(UiText.DynamicString(it.data?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun forgetPass(user_email: String): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow { EventsApi.forgetPass(user_email) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(Resource.Error(UiText.DynamicString(model?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun updatePass(
        current_pass: String,
        user_pass: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                EventsApi.updatePass(
                    UserUtil.getUserId(),
                    current_pass,
                    user_pass
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(Resource.Error(UiText.DynamicString(model?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun allCountries(): Flow<Resource<List<CountriesModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.allCountries() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allCountries?.map { it.toCountriesModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun allCities(countryId: String): Flow<Resource<List<CitiesModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.allCities(countryId) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allCities?.map { it.toCitiesModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun allRegions(cityId: String): Flow<Resource<List<RegionsModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.allRegions(cityId) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allRegions?.map { it.toRegionsModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }
}