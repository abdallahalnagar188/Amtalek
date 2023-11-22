package eramo.amtalek.domain.usecase.drawer

import eramo.amtalek.data.remote.dto.drawer.myaccount.EditProfileResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.DrawerRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateProfileUseCase @Inject constructor(private val repository: DrawerRepository) {
    suspend operator fun invoke(
        firstName: RequestBody?,
        lastName: RequestBody?,
        mobileNumber: RequestBody?,
        email: RequestBody?,
        countryId: RequestBody?,
        cityId: RequestBody?,
        bio: RequestBody?,
        profileImage: MultipartBody.Part?,
        coverImage: MultipartBody.Part?
    ): Flow<Resource<ResultModel>> {
        return repository.updateProfile(
            firstName, lastName, mobileNumber, email, countryId, cityId, bio, profileImage, coverImage
        )
    }
}