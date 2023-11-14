package eramo.amtalek.presentation.viewmodel.drawer.myaccount

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.drawer.myaccount.EditProfileResponse
import eramo.amtalek.domain.model.auth.CitiesModel
import eramo.amtalek.domain.model.auth.CountriesModel
import eramo.amtalek.domain.model.auth.RegionsModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.domain.usecase.drawer.EditProfileUseCase
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditPersonalDetailsViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _editProfileState = MutableStateFlow<UiState<EditProfileResponse>>(UiState.Empty())
    val editProfileState: StateFlow<UiState<EditProfileResponse>> = _editProfileState

    private val _countriesState = MutableStateFlow<UiState<List<CountriesModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountriesModel>>> = _countriesState

    private val _cityState = MutableStateFlow<UiState<List<CitiesModel>>>(UiState.Empty())
    val cityState: StateFlow<UiState<List<CitiesModel>>> = _cityState

    private val _regionState = MutableStateFlow<UiState<List<RegionsModel>>>(UiState.Empty())
    val regionState: StateFlow<UiState<List<RegionsModel>>> = _regionState

    private var editProfileJob: Job? = null
    private var countryJob: Job? = null
    private var cityJob: Job? = null
    private var regionJob: Job? = null

    fun cancelRequest() {
        editProfileJob?.cancel()
        countryJob?.cancel()
        cityJob?.cancel()
        regionJob?.cancel()
    }

    fun editProfile(
        user_id: String,
        user_pass: String,
        user_name: String,
        address: String,
        countryId: String,
        cityId: String,
        regionId: String,
        user_email: String,
        user_phone: String,
        m_image: Uri?
    ) {
        editProfileJob?.cancel()
        editProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                editProfileUseCase(
                    convertToRequestBody(user_id),
                    convertToRequestBody(user_pass),
                    convertToRequestBody(user_name),
                    convertToRequestBody(address),
                    convertToRequestBody(countryId),
                    convertToRequestBody(cityId),
                    convertToRequestBody(regionId),
                    convertToRequestBody(user_email),
                    convertToRequestBody(user_phone),
                    convertFileToMultipart(m_image)
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _editProfileState.value = UiState.Success(it)
                            } ?: run { _editProfileState.value = UiState.Empty() }
                            saveUserInfo(
                                user_name,
                                user_pass,
                                address,
                                countryId,
                                cityId,
                                regionId,
                                user_email,
                                user_phone,
                                result.data?.member?.mImage ?: ""
                            )
                        }
                        is Resource.Error -> {
                            _editProfileState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _editProfileState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun countries() {
        countryJob?.cancel()
        countryJob = viewModelScope.launch {
            withContext(coroutineContext) {
//                authRepository.allCountries().collect {
//                    when (it) {
//                        is Resource.Success -> {
//                            _countriesState.value = UiState.Success(it.data)
//                        }
//                        is Resource.Error -> {
//                            _countriesState.value = UiState.Error(it.message!!)
//                        }
//                        is Resource.Loading -> {
//                            _countriesState.value = UiState.Loading()
//                        }
//                    }
//                }
            }
        }
    }

    fun cities(countryId: Int) {
        countryJob?.cancel()
        countryJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.allCities(countryId.toString()).collect {
                    when (it) {
                        is Resource.Success -> {
                            _cityState.value = UiState.Success(it.data)
                        }
                        is Resource.Error -> {
                            _cityState.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading -> {
                            _cityState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun regions(cityId: Int) {
        countryJob?.cancel()
        countryJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.allRegions(cityId.toString()).collect {
                    when (it) {
                        is Resource.Success -> {
                            _regionState.value = UiState.Success(it.data)
                        }
                        is Resource.Error -> {
                            _regionState.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading -> {
                            _regionState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun convertToRequestBody(part: String): RequestBody? {
        return try {
            RequestBody.create(MediaType.parse("multipart/form-data"), part)
        } catch (e: Exception) {
            null
        }
    }

    private fun convertFileToMultipart(imageUri: Uri?): MultipartBody.Part? {
        return if (imageUri != null) {
            val file = File(imageUri.path!!)
            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
            MultipartBody.Part.createFormData("m_image", file.name, requestBody)
        } else null
    }

    private fun saveUserInfo(
        user_name: String,
        user_pass: String,
        address: String,
        countryId: String,
        cityId: String,
        regionId: String,
        user_email: String,
        user_phone: String,
        m_image: String
    ) {
        UserUtil.saveUserInfo(
            UserUtil.isRememberUser(),
            UserUtil.getUserId(),
            UserUtil.getUserToken(),
            user_name,
            user_pass,
            address,
            countryId,
            cityId,
            regionId,
            user_phone,
            user_email,
            m_image,
        )
    }
}