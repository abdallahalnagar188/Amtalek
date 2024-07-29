package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.myHome.allCitys.AllCityResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse

interface AllCitiesRepo {
        suspend fun getAllCitiesFromRemote(): AllCityResponse
}