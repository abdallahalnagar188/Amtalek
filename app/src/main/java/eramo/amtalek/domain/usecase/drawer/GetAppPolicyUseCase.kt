package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.domain.model.drawer.PolicyInfoModel
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAppPolicyUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(): Flow<Resource<List<PolicyInfoModel>>> {
        return repository.getAppPolicy()
    }
}