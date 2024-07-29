package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.allCitys.AllCityResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.domain.repository.AllCitiesRepo
import eramo.amtalek.domain.repository.AllPropertyRepo

class AllCitiesRepoImpl(private val apiService:AmtalekApi) :AllCitiesRepo{
    override suspend fun getAllCitiesFromRemote(): AllCityResponse {
        return apiService.getAllCities()
    }
}