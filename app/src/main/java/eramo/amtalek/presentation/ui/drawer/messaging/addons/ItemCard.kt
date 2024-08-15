package eramo.amtalek.presentation.ui.drawer.messaging.addons

import eramo.amtalek.data.remote.dto.adons.Data
import java.io.Serializable

data class ItemCard(
    val card: List<Data>,
    val totalPrice: Int,
    val deuration: String,
): Serializable
