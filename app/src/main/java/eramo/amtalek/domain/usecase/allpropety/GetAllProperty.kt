package eramo.amtalek.domain.usecase.allpropety

import eramo.amtalek.data.remote.dto.myHome.featured_properety.HomeFeaturedPropertiesResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.domain.repository.AllPropertyRepo
import eramo.amtalek.domain.repository.BrokersDetailsRepo
import eramo.amtalek.domain.repository.MyHomeRepository
import javax.inject.Inject

class GetAllProperty @Inject constructor(private val allPropertyResponse: AllPropertyRepo) {
    suspend operator fun invoke() = allPropertyResponse.getAllPropertiesFromRemote()
}