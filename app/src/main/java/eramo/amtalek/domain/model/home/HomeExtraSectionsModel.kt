package eramo.amtalek.domain.model.home

import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel

data class HomeExtraSectionsModel (
    var title: String,
    var sections: List<PropertyModel>?
)