package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.adons.BuyAddonsResponse
import eramo.amtalek.domain.repository.BuyAddonsRepo
import eramo.amtalek.presentation.ui.drawer.messaging.addons.CardReqoest
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BuyAddonsRepoImpl(private val apiService: AmtalekApi): BuyAddonsRepo {
    override suspend fun buyAddons(
        list: CardReqoest,
    ): Flow<Resource<BuyAddonsResponse>> {
        return flow {
            val result = toResultFlow {
                apiService.buyAddons(userToken =UserUtil.getUserToken(), list)
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