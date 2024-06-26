package eramo.amtalek.domain.model.property.addpropertymodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPropertyThirdModel (
    val addPropertySecondModel: AddPropertySecondModel,
    val purposeId:Int,
    val categoryId:Int,
    val priorityId:String,
    val countryId:Int,
    val cityId:Int,
    val regionId:Int,
    val subRegionId:Int,
    val propertyType:Int,
    val finishingId:Int,
    val floorFinishingId:Int
) : Parcelable