package eramo.amtalek.presentation.viewmodel.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.domain.usecase.drawer.GetProfileUseCase
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _getProfileState = MutableStateFlow<Resource<ProfileModel>>(Resource.Loading())
    val getProfileState: StateFlow<Resource<ProfileModel>> = _getProfileState

    private var getProfileJob: Job? = null

    fun cancelRequest() {
        getProfileJob?.cancel()
    }

    fun getProfile(type: String, id: String) {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                getProfileUseCase(type, id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            saveUserInfo(result.data?.data?.data?.toProfile()!!)
                            _getProfileState.value = Resource.Success(result.data?.data?.data?.toProfile())
                        }

                        is Resource.Error -> {
                            _getProfileState.value =
                                Resource.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _getProfileState.value = Resource.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserInfo(user: ProfileModel) {
        UserUtil.saveUserInfo(
            isRemember = true,
            userToken = UserUtil.getUserToken(),
            userID = user.id.toString(),
            firstName = user.firstName,
            lastName = user.lastName,
            phone = user.phone,
            email = user.email,
            countryId = user.countryId.toString(),
            countryName = user.countryName,
            cityName = user.cityName.toString(),
            cityId = user.cityId.toString(),
            userBio = user.bio,
            profileImageUrl = user.image,
            userType = user.actorType ?: "",
            hasPackage = user.hasPackage,
            brokerName = "",
            dashboardLink = user.dashboardLink ?: "",
            packageId = (user.currentPackage?.packageId?:"0").toString()
        )
    }
}