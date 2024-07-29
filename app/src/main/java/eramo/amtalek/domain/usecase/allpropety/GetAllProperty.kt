package eramo.amtalek.domain.usecase.allpropety

import eramo.amtalek.domain.repository.AllPropertyRepo
import javax.inject.Inject

class GetAllProperty @Inject constructor(private val allPropertyResponse: AllPropertyRepo) {
    suspend operator fun invoke() = allPropertyResponse.getAllPropertiesFromRemote()
}