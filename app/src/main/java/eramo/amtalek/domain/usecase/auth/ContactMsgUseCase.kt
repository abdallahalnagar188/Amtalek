package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactUsUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun getContactUsInfo(): Flow<Resource<ContactUsInfoModel>> {
        return repository.getContactUsInfo()
    }
}