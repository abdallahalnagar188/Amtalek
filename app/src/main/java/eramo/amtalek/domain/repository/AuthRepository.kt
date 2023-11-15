package eramo.amtalek.domain.repository

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.*
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Field

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

    suspend fun loginApp(user_phone: String, user_pass: String): Flow<Resource<LoginModel>>

    suspend fun forgetPass(user_email: String): Flow<Resource<ResultModel>>

    suspend fun updatePass(
        current_pass: String,
        user_pass: String
    ): Flow<Resource<ResultModel>>

    suspend fun getCountries(): Flow<Resource<List<CountryModel>>>

    suspend fun getCities(countryId: String): Flow<Resource<List<CityModel>>>

    suspend fun allRegions(cityId: String): Flow<Resource<List<RegionsModel>>>

}