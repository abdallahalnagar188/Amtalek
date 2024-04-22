package eramo.amtalek.presentation.viewmodel.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.usecase.auth.CountriesAndCitiesUseCase
import eramo.amtalek.domain.usecase.auth.RegisterUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val countriesAndCitiesUseCase: CountriesAndCitiesUseCase
) : ViewModel() {

    private val _countriesState = MutableStateFlow<UiState<List<CountryModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountryModel>>> = _countriesState

    private val _citiesState = MutableStateFlow<UiState<List<CityModel>>>(UiState.Empty())
    val citiesState: StateFlow<UiState<List<CityModel>>> = _citiesState
    private val _regionsState = MutableStateFlow<UiState<List<RegionModel>>>(UiState.Empty())
    val regionsState: StateFlow<UiState<List<RegionModel>>> = _regionsState

    private val _registerState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val registerState: StateFlow<UiState<ResultModel>> = _registerState

    private val _sendVerificationCodeEmailState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val sendVerificationCodeEmailState: StateFlow<UiState<ResultModel>> = _sendVerificationCodeEmailState

    var registeredEmail: String? = null

    private var getCountriesJob: Job? = null
    private var getCitiesJob: Job? = null
    private var registerJob: Job? = null
    private var sendVerificationCodeEmailJob: Job? = null

    fun cancelRequest() {
        getCountriesJob?.cancel()
        getCitiesJob?.cancel()
        registerJob?.cancel()
        sendVerificationCodeEmailJob?.cancel()
    }
    fun convertToRequestBody(part: String?): RequestBody? {
        return try {
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part!!)
        } catch (e: Exception) {
            null
        }

    }

    private fun convertFileToMultipart(img_keyName: String, imageUri: Uri?): MultipartBody.Part? {
        return if (imageUri != null) {
            val file = File(imageUri.path!!)
            val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData(img_keyName, file.name, requestBody)
        } else null
    }
    fun register(
        firstName: String?,
        lastName: String?,
        phone: String?,
        email: String?,
        birthday:String?,
        password: String?,
        confirmPassword: String?,
        gender: String?,
        countryId: String?,
        cityId: String?,
        regionId:String?,
        companyName: String?,
        iam:String?,
        companyLogo: Uri?
    ) {
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            withContext(coroutineContext) {

                registeredEmail = email

                    registerUseCase.register(
                        firstName = convertToRequestBody(firstName), lastName = convertToRequestBody(lastName), phone =  convertToRequestBody(phone), email =  convertToRequestBody(email),
                        password = convertToRequestBody(password), confirmPassword = convertToRequestBody(confirmPassword) ,
                       gender =  convertToRequestBody(gender), countryId =  convertToRequestBody(countryId), cityId = convertToRequestBody(cityId),
                        regionId = convertToRequestBody(regionId),
                        companyLogo = convertFileToMultipart("company_logo",companyLogo), companyName = convertToRequestBody(companyName), iam = convertToRequestBody(iam),
                        birthday = convertToRequestBody(birthday)
                    ).collect {
                        when (it) {
                            is Resource.Success -> {
                                _registerState.value = UiState.Success(it.data)
                            }

                            is Resource.Error -> {
                                _registerState.value = UiState.Error(it.message!!)
                            }

                            is Resource.Loading -> {
                                _registerState.value = UiState.Loading()
                            }
                        }
                    }

            }
        }
    }

    fun sendVerificationCodeEmail() {
        sendVerificationCodeEmailJob?.cancel()
        sendVerificationCodeEmailJob = viewModelScope.launch {
            withContext(coroutineContext) {
                registeredEmail?.let { email ->
                    registerUseCase.sendVerificationCodeEmail(email).collect {
                        when (it) {
                            is Resource.Success -> {
                                _sendVerificationCodeEmailState.value = UiState.Success(it.data)
                            }

                            is Resource.Error -> {
                                _sendVerificationCodeEmailState.value = UiState.Error(it.message!!)
                            }

                            is Resource.Loading -> {
                                _sendVerificationCodeEmailState.value = UiState.Loading()
                            }
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
    fun getRegions(cityId: String) {
        getCitiesJob?.cancel()
        getCitiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getRegions(cityId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _regionsState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _regionsState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _regionsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }


}