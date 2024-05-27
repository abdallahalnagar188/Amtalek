package eramo.amtalek.presentation.ui.interfaces

import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel

interface FavClickListener {
    fun onFavClick(model: PropertyModel)
}