package eramo.amtalek.domain.usecase.allpropety

import eramo.amtalek.domain.repository.AllNormalPropertiesRepo
import javax.inject.Inject

class GetAllNormalProperty @Inject constructor(private val allPropertyResponse: AllNormalPropertiesRepo) {
    suspend operator fun invoke() = allPropertyResponse.getAllNormalPropertiesFromRemote()
}