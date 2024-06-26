package eramo.amtalek.domain.model.property.addpropertymodels

import android.os.Parcelable
import eramo.amtalek.domain.model.project.AmenityModelSelection
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPropertyFifthModel(
    val addPropertyFourthModel: AddPropertyFourthModel,
    val amenitiesList:List<AmenityModelSelection>
) : Parcelable