package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firebaseToken: String
    ): Flow<Resource<UserModel>> {
        val isBlank = email.isBlank() || password.isBlank() || firebaseToken.isBlank()

        return if (isBlank) flow { }
        else repository.login(email, password, firebaseToken)
    }
}