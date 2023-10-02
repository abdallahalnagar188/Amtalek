package eramo.amtalek.domain.usecase.auth

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(
        user_name: String,
        user_email: String,
        user_phone: String,
        user_pass: String,
        address: String,
        countryId: String,
        cityId: String,
        regionId: String,
        gender: String
    ): Flow<Resource<ResultModel>> {
        val isBlank = user_name.isBlank() || user_email.isBlank()
                || user_phone.isBlank() || user_pass.isBlank() || address.isBlank()

        return if (isBlank) flow { }
        else repository.register(
            user_name,
            user_email,
            user_phone,
            user_pass,
            address,
            countryId,
            cityId,
            regionId,
            gender
        )
    }
}