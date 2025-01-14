package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getHome(): Flow<Resource<HomeResponse>>

    suspend fun getHomeFilteredByCity(cityId:String): Flow<Resource<HomeResponse>>
}