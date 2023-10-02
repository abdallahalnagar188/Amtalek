package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactMsgUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(
        user_name: String,
        user_email: String,
        user_phone: String,
        subject: String,
        details: String
    ): Flow<Resource<ResultModel>> {
        val isBlank = user_name.isBlank() || user_email.isBlank() || user_phone.isBlank()
                || subject.isBlank() || details.isBlank()
        return if (isBlank) flow { }
        else repository.contactMsg(user_name, user_email, user_phone, subject, details)
    }
}