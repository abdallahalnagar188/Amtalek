package eramo.amtalek.presentation.viewmodel.drawer.myaccount

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.drawer.myaccount.GetProfileResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.GetProfileModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.usecase.auth.CountriesAndCitiesUseCase
import eramo.amtalek.domain.usecase.drawer.GetProfileUseCase
import eramo.amtalek.domain.usecase.drawer.UpdateProfileUseCase
import eramo.amtalek.util.UPLOAD_COVER_IMAGE_KEY
import eramo.amtalek.util.UPLOAD_PROFILE_IMAGE_KEY
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditPersonalDetailsViewModel @Inject constructor(
    private val countriesAndCitiesUseCase: CountriesAndCitiesUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _getProfileState = MutableStateFlow<UiState<UserModel>>(UiState.Empty())
    val getProfileState: StateFlow<UiState<UserModel>> = _getProfileState

    private val _updateProfileState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val updateProfileState: StateFlow<UiState<ResultModel>> = _updateProfileState

    private val _countriesState = MutableStateFlow<UiState<List<CountryModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountryModel>>> = _countriesState

    private val _citiesState = MutableStateFlow<UiState<List<CityModel>>>(UiState.Empty())
    val citiesState: StateFlow<UiState<List<CityModel>>> = _citiesState

    private var getProfileJob: Job? = null
    private var updateProfileJob: Job? = null
    private var getCountriesJob: Job? = null
    private var getCitiesJob: Job? = null

    fun cancelRequest() {
        getProfileJob?.cancel()
        updateProfileJob?.cancel()
        getCountriesJob?.cancel()
        getCitiesJob?.cancel()
    }

    private fun getProfile(type:String,id:String) {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                getProfileUseCase(type = type, id = id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            saveUserInfo(result.data?.toUserModel()!!)
                            _getProfileState.value = UiState.Success(result.data.toUserModel())
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

    fun updateProfile(
        firstName: String,
        lastName: String,
        mobileNumber: String,
        email: String,
        countryId: String,
        cityId: String,
        bio: String,
        profileImage: Uri?,
        coverImage: Uri?
    ) {
        updateProfileJob?.cancel()
        updateProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                updateProfileUseCase(
                    convertToRequestBody(firstName),
                    convertToRequestBody(lastName),
                    convertToRequestBody(mobileNumber),
                    convertToRequestBody(email),
                    convertToRequestBody(countryId),
                    convertToRequestBody(cityId),
                    convertToRequestBody(bio),
                    convertFileToMultipart(profileImage, UPLOAD_PROFILE_IMAGE_KEY),
                    convertFileToMultipart(coverImage, UPLOAD_COVER_IMAGE_KEY)
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _updateProfileState.value = UiState.Success(result.data)

                            getProfile(UserUtil.getUserType(),UserUtil.getUserId())
                        }

                        is Resource.Error -> {
                            _updateProfileState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _updateProfileState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getCountries() {
        getCountriesJob?.cancel()
        getCountriesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getCountries().collect {
                    when (it) {
                        is Resource.Success -> {
                            _countriesState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _countriesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _countriesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getCities(countryId: String) {
        getCitiesJob?.cancel()
        getCitiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getCities(countryId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _citiesState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _citiesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _citiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun convertToRequestBody(part: String): RequestBody? {
        return try {
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part)
        } catch (e: Exception) {
            null
        }
    }

    private fun convertFileToMultipart(imageUri: Uri?, key: String): MultipartBody.Part? {
        return if (imageUri != null) {
            val file = File(imageUri.path!!)
            val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData(key, file.name, requestBody)
        } else null
    }

    private fun saveUserInfo(user: UserModel) {
        UserUtil.saveUserInfo(
            isRemember = true,
           userToken =  UserUtil.getUserToken(), userID =  user.id.toString(),
           firstName =  user.firstName?:"", lastName =  user.lastName?:"", phone =  user.phone?:"",
           email =  user.email?:"", countryId =  user.country.toString()?:"",
           countryName =  user.countryName?:"", cityId = user.city.toString(), cityName = user.cityName?:"", userBio =  user.bio?:"", profileImageUrl = user.userImage?:"",
            userType = user.actorType?:""
        )
    }

    init {
        getProfile(UserUtil.getUserType(),UserUtil.getUserId())
    }
}