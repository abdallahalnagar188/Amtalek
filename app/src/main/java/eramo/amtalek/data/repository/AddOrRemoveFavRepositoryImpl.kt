package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.domain.repository.AddOrRemoveFavRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddOrRemoveFavRepositoryImpl @Inject constructor(
    private val amtalekApi: AmtalekApi
):AddOrRemoveFavRepository {
    override suspend fun addOrRemoveFav(propertyId: Int): Flow<Resource<AddOrRemoveFavResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.addOrRemoveFavProperty(
                    userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    propertyId
                )
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
}