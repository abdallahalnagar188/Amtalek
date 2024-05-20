package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.domain.repository.ProjectRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):ProjectRepository {
    override suspend fun getProjectDetails(listingNumber: String): Flow<Resource<ProjectDetailsResponse>> {
        return flow {
            val result = toResultFlow { amtalekApi.getProjectDetails(listingNumber)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        emit(Resource.Success(it.data))
                    }
                }

            }
        }
    }
}