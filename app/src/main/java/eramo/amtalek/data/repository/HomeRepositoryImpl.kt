package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.domain.repository.HomeRepository
import eramo.amtalek.util.FROM_ANDROID
import eramo.amtalek.util.SIGN_UP_GENDER_ACCEPT_CONDITION
import eramo.amtalek.util.SIGN_UP_GENDER_ACCEPT_NOT_ROBOT
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl (private val amtalekApi: AmtalekApi) : HomeRepository {

    override suspend fun getHome(): Flow<Resource<HomeResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getHome(if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }
}