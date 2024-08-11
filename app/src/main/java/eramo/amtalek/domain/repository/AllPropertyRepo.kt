package eramo.amtalek.domain.repository

import androidx.paging.PagingSource
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.data.remote.dto.property.allproperty.DataX

interface AllPropertyRepo {
        suspend fun getAllPropertiesFromRemote(): AllPropertyResponse
        fun getAllPropertiesPagingSource(): PagingSource<Int, DataX>

}