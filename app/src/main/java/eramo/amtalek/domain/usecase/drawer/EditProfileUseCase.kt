package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.data.remote.dto.drawer.myaccount.EditProfileResponse
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditProfileUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(
        user_id: RequestBody?,
        user_pass: RequestBody?,
        user_name: RequestBody?,
        address: RequestBody?,
        countryId: RequestBody?,
        cityId: RequestBody?,
        regionId: RequestBody?,
        user_email: RequestBody?,
        user_phone: RequestBody?,
        m_image: MultipartBody.Part?
    ): Flow<Resource<EditProfileResponse>> {
        return repository.editProfile(
            user_id,
            user_pass,
            user_name,
            address,
            countryId,
            cityId,
            regionId,
            user_email,
            user_phone,
            m_image
        )
    }
}