package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesAndCitiesUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun getCountries(): Flow<Resource<List<CountryModel>>> {
        return repository.getCountries()
    }

    suspend fun getCities(countryId: String): Flow<Resource<List<CityModel>>> {
        return repository.getCities(countryId)
    }
}