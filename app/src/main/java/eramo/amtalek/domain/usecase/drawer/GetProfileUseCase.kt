package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.data.remote.dto.drawer.myaccount.GetProfileResponse
import eramo.amtalek.domain.model.auth.GetProfileModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProfileUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke():  Flow<Resource<GetProfileResponse>>{
        return repository.getProfile()
    }
}