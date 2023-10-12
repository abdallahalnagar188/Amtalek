package eramo.amtalek.presentation.viewmodel.auth

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class OtpForgetPasswordViewModel @Inject constructor() : ViewModel() {

    private lateinit var timer: CountDownTimer
    private val _timerState = MutableStateFlow("")
    val timerState: StateFlow<String> = _timerState

    fun startTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = formatMilliseconds(millisUntilFinished)
            }

            override fun onFinish() {

            }
        }
        timer.start()
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val formattedSeconds = DecimalFormat("00").format(seconds)
        return "$minutes:$formattedSeconds"
    }

    init {
        startTimer()
    }
}