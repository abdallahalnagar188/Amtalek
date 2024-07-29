package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.domain.repository.AllNormalPropertiesRepo
import eramo.amtalek.domain.repository.AllPropertyRepo

class AllNormalPropertiesRepoImpl(private val apiService:AmtalekApi) :AllNormalPropertiesRepo{
    override suspend fun getAllNormalPropertiesFromRemote(): AllPropertyResponse {
        return apiService.getAllNormalProperties()
    }
}