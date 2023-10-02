package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateFirebaseDeviceTokenUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(deviceToken: String): Flow<Resource<ResultModel>> {
        return repository.updateFirebaseDeviceToken(deviceToken)
    }
}