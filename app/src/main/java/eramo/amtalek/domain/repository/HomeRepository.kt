package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getHome(): Flow<Resource<HomeResponse>>

}