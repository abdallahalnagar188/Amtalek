package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.splash.SplashResponse
import eramo.amtalek.data.remote.dto.splash.splashV2.OnBordingResponse
import eramo.amtalek.domain.repository.SplashRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashRepoImpl@Inject constructor(
    val amtalekApi: AmtalekApi
)  : SplashRepo {
    override suspend fun getOnBoardingSlider(): Flow<Resource<OnBordingResponse?>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getOnBoardingSlider()
            }
            result.collect() {
                when (it) {
                    is ApiState.Success -> {
                        emit(Resource.Success(it.data))
                    }

                    is ApiState.Error -> {
                        emit(Resource.Error(it.message!!))
                    }

                    is ApiState.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }
        }
    }
}