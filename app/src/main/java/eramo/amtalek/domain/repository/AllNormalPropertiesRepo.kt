package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse

interface AllNormalPropertiesRepo {
    suspend fun getAllNormalPropertiesFromRemote():AllPropertyResponse
}