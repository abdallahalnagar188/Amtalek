package eramo.amtalek.data.remote.dto.adons

import eramo.amtalek.presentation.ui.drawer.messaging.addons.ItemCard
import java.io.Serializable

data class BuyAddonsRequest (
    val addons: List<ItemCard>
): Serializable

