package eramo.amtalek.domain.model.home.project

import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel

data class HomeProjectsSectionModel(
    val title: String,
    val projects: List<ProjectModel>?
)