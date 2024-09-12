package eramo.amtalek.data.repository.search

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.search.currencies.CurrenciesResponse
import eramo.amtalek.domain.repository.search.CurrenciesRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):CurrenciesRepository {
    override suspend fun getAllCurrencies(): Flow<Resource<CurrenciesResponse>> {
        return flow {
            val result = toResultFlow { amtalekApi.getCurrencies() }
            result.collect(){
                when(it){
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