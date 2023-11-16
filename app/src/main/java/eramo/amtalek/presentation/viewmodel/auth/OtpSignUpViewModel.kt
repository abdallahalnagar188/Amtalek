package eramo.amtalek.presentation.viewmodel.auth

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class OtpSignUpViewModel @Inject constructor() : ViewModel() {

    private val _timerState = MutableStateFlow("")
    val timerState: StateFlow<String> = _timerState

    private val _enableResendEvent = Channel<Boolean>()
    val enableResendEvent = _enableResendEvent.receiveAsFlow()

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

    init {
        startTimer()
    }
}