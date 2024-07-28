package eramo.amtalek.presentation.ui.interfaces

import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel

interface FavClickListener {
    fun onFavClick(model: PropertyModel)
}
interface FavClickListenerOriginalItem{
    fun onFavClick(model:OriginalItem)
}