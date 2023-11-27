package eramo.amtalek.presentation.viewmodel.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.UserModel
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

    private val _getProfileState = MutableStateFlow<UiState<UserModel>>(UiState.Empty())
    val getProfileState: StateFlow<UiState<UserModel>> = _getProfileState

    private var getProfileJob: Job? = null

    fun cancelRequest() {
        getProfileJob?.cancel()
    }

    fun getProfile() {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                getProfileUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            saveUserInfo(result.data!!)
                            _getProfileState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _getProfileState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _getProfileState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserInfo(user: UserModel) {
        UserUtil.saveUserInfo(
            true,
            UserUtil.getUserToken(), user.id.toString(),
            user.firstName, user.lastName, user.phone,
            user.email, user.countryId.toString(),
            user.countryName, user.cityId.toString(), user.cityName, user.bio, user.profileImageUrl, user.coverImageUrl
        )
    }
}