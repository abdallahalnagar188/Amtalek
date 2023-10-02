package eramo.amtalek.domain.model.dummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumModel(
    val name: String? = null,
    val resId: Int? = null,
    val imgUrl: String? = null
) : Parcelable