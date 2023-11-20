package eramo.amtalek.presentation.viewmodel.auth

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.domain.usecase.auth.ForgotPassUseCase
import eramo.amtalek.util.API_OPERATION_TYPE_FORGET_PASSWORD
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class OtpForgetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val forgotPassUseCase: ForgotPassUseCase
) : ViewModel() {


    private val _timerState = MutableStateFlow("")
    val timerState: StateFlow<String> = _timerState

    private val _enableResendEvent = Channel<Boolean>()
    val enableResendEvent = _enableResendEvent.receiveAsFlow()

    private val _checkOtpCodeState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val checkOtpCodeState: StateFlow<UiState<ResultModel>> = _checkOtpCodeState

    private val _resendForgotPasswordMailState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val resendForgotPasswordMailState: StateFlow<UiState<ResultModel>> = _resendForgotPasswordMailState

    private var checkOtpCodeJob: Job? = null
    private var resendForgotPassMailJob: Job? = null

    fun cancelRequest() {
        checkOtpCodeJob?.cancel()
        resendForgotPassMailJob?.cancel()
    }

    fun startTimer() {
        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = formatMilliseconds(millisUntilFinished)
            }

            override fun onFinish() {
                viewModelScope.launch {
                    _enableResendEvent.send(true)
                }
            }
        }
        timer.start()
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val formattedSeconds = DecimalFormat("00").format(seconds)
        val formattedMinutes = DecimalFormat("00").format(minutes)
        return "$formattedMinutes:$formattedSeconds"
    }

    fun checkOtpCode(
        email: String,
        otpCode: String,
    ) {
        checkOtpCodeJob?.cancel()
        checkOtpCodeJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.checkOtpCode(email, otpCode, API_OPERATION_TYPE_FORGET_PASSWORD).collect {
                    when (it) {
                        is Resource.Success -> {
                            _checkOtpCodeState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _checkOtpCodeState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _checkOtpCodeState.value = UiState.Loading()
                        }
                    }
                }
            }
        }

    }

    fun resendForgotPasswordMail(email: String) {
        resendForgotPassMailJob?.cancel()
        resendForgotPassMailJob = viewModelScope.launch {
            withContext(coroutineContext) {
                forgotPassUseCase.sendForgetPasswordCodeEmail(email).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _resendForgotPasswordMailState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _resendForgotPasswordMailState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _resendForgotPasswordMailState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    init {
        startTimer()
    }
}