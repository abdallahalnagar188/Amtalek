package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getProjectDetails(listingNumber:String): Flow<Resource<ProjectDetailsResponse>>
}