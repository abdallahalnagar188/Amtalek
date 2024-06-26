package eramo.amtalek.domain.model.property.addpropertymodels

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPropertyFourthModel(
    val addPropertyThirdModel: AddPropertyThirdModel,
    val primaryImage: Uri,
    val sliderImages: List<Uri>,
    val descriptionInEnglish:String,
    val descriptionInArabic:String
) : Parcelable
