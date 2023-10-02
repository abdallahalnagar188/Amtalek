package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForgotPassUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(
        user_email: String
    ): Flow<Resource<ResultModel>> {
        return if (user_email.isBlank()) flow { }
        else repository.forgetPass(user_email)
    }
}