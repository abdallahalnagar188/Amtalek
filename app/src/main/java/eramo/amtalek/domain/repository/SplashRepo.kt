package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.splash.SplashResponse
import eramo.amtalek.data.remote.dto.splash.splashV2.OnBordingResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface SplashRepo {
    suspend fun getOnBoardingSlider(): Flow<Resource<OnBordingResponse?>>
}