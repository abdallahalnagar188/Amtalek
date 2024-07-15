package eramo.amtalek.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CriteriaModel (
    val id:Int,
    val propertyCount :Int?,
    val title:String
) : Parcelable
