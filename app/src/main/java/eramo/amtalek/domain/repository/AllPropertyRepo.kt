package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse

interface AllPropertyRepo {
        suspend fun getAllPropertiesFromRemote(): AllPropertyResponse
}