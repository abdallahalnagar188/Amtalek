package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.hotoffers.HotOffersResponse
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.domain.repository.HotOffersRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HotOffersRepositoryImpl @Inject constructor(
    private val amtalekApi: AmtalekApi
):HotOffersRepository {
    override suspend fun getHotOffers(countryId:String?): Flow<Resource<HotOffersResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHotOffers(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null
                ,countryId = countryId)

            }
            result.collect(){
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

    override suspend fun getHotOffersSlider(): Flow<Resource<HomeSlidersResponse>> {
        return flow {
            val result =
                toResultFlow { amtalekApi.getHotOffersSlider() }
            result.collect() {
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