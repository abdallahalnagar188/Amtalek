package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.domain.model.home.property.HomePropertySectionModel

interface AllNormalPropertiesRepo {
    suspend fun getAllNormalPropertiesFromRemote(): AllPropertyResponse
}