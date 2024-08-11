package eramo.amtalek.domain.repository

import androidx.paging.PagingSource
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.domain.model.home.property.HomePropertySectionModel

interface AllNormalPropertiesRepo {
    suspend fun getAllNormalPropertiesFromRemote(): AllPropertyResponse
    fun getAllNormalPropertiesPagingSource(): PagingSource<Int, DataX>
}