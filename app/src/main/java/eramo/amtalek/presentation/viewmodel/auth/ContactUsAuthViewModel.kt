package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.domain.usecase.auth.ContactUsUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ContactUsAuthViewModel @Inject constructor(
    private val contactUsUseCase: ContactUsUseCase
) : ViewModel() {

    private val _getContactUsInfoState = MutableStateFlow<UiState<ContactUsInfoModel>>(UiState.Empty())
    val getContactUsInfoState: StateFlow<UiState<ContactUsInfoModel>> = _getContactUsInfoState

    private var _getContactUsInfoJob: Job? = null

    fun cancelRequest() = _getContactUsInfoJob?.cancel()

    private fun getContactUsInfo() {
        _getContactUsInfoJob?.cancel()
        _getContactUsInfoJob = viewModelScope.launch {
            withContext(coroutineContext) {
                contactUsUseCase.getContactUsInfo().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _getContactUsInfoState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _getContactUsInfoState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _getContactUsInfoState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    init {
        getContactUsInfo()
    }
}