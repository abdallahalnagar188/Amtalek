package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.packages.PackagesResponse
import eramo.amtalek.domain.repository.PackagesRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PackagesRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):PackagesRepository{
    override suspend fun getPackages(type: String): Flow<Resource<PackagesResponse>> {
        return flow {
            val result = toResultFlow { amtalekApi.getPackages(type) }

            result.collect(){
                when (it){
                    is ApiState.Success->{
                            emit(Resource.Success(it.data!!))
                    }
                    is ApiState.Error->{
                        emit(Resource.Error(it.message!!))
                    }
                    is ApiState.Loading->{
                        emit(Resource.Loading())
                    }
                }
        }
        }
    }
}