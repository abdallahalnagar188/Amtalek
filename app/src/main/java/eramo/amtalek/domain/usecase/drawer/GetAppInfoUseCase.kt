package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAppInfoUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(): Flow<Resource<AppInfoResponse>> {
        return repository.getAppInfo()
    }
}