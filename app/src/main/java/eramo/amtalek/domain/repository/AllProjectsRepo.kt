package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.project.allProjects.AllProjectsResponse

interface AllProjectsRepo {
    suspend fun getAllProjectsFromRemote(): AllProjectsResponse
}