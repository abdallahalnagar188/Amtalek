package eramo.amtalek.domain.usecase.allprojects

import eramo.amtalek.domain.repository.AllProjectsRepo
import javax.inject.Inject

class GetAllProjects @Inject constructor(private val allProjectsResponse: AllProjectsRepo) {
    suspend operator fun invoke() = allProjectsResponse.getAllProjectsFromRemote()
}