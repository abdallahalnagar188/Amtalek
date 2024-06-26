package eramo.amtalek.domain.model.property.addpropertymodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPropertyFirstModel(
    val propertyNameInEnglish:String,
    val propertyNameInArabic:String,
    val propertyAddressInEnglish:String,
    val propertyAddressInArabic:String,
    val propertyVideoUrl:String?,
    val propertyLocation:String,
) : Parcelable