package eramo.amtalek.domain.search

import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel

data class SearchResponseModel(
    val propList:List<PropertyModel>,
    val featuredCount:Int,
    val normalCount:Int,
    val totalProps:Int
)
