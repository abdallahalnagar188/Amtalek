package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProfileUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(): Flow<Resource<Member>> {
        return repository.getProfile()
    }
}