package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.domain.repository.AllPropertyRepo

class AllPropertiesRepoImpl(private val apiService:AmtalekApi) :AllPropertyRepo{
    override suspend fun getAllPropertiesFromRemote(): AllPropertyResponse {
        return apiService.getAllProperties()
    }
}