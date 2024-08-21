package eramo.amtalek.presentation.ui.drawer.messaging.addons

import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.adons.Data
import java.io.Serializable

data class ItemCard(
    val card: List<Data>,
    val totalPrice: Int = 0,
    val deuration: String = "",
): Serializable

data class CardReqoest(
    @SerializedName("addons")
    val card: List<AddreesItem>,
    @SerializedName("duration")
    val deuration: String = "",
): Serializable

data class AddreesItem(
    val quantity: Int = 0,
    val id: Int = 0,
): Serializable