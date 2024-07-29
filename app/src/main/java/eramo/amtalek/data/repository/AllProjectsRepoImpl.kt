package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.project.allProjects.AllProjectsResponse
import eramo.amtalek.domain.repository.AllProjectsRepo

class AllProjectsRepoImpl(private val apiService:AmtalekApi):AllProjectsRepo {
    override suspend fun getAllProjectsFromRemote(): AllProjectsResponse {
        return apiService.getAllProjects()
    }

}