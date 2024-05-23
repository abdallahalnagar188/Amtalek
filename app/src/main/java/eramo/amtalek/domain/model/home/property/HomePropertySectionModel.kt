package eramo.amtalek.domain.model.home.property

import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel

data class HomePropertySectionModel(
    var title: String,
    var propertiesList: List<PropertyModel>?
)