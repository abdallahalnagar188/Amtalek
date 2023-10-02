package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePassUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(
        current_pass: String,
        user_pass: String
    ): Flow<Resource<ResultModel>> {
        val isBlank = current_pass.isBlank() || user_pass.isBlank()

        return if (isBlank) flow { }
        else repository.updatePass(current_pass, user_pass)
    }
}