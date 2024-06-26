package eramo.amtalek.domain.model.property.addpropertymodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPropertySecondModel(
    val addPropertyFirstModel: AddPropertyFirstModel,
    val buildingNumber: Int,
    val floorNumber: Int,
    val numberFloor:Int,
    val unitNumber: Int,
    val livingRoomNumber: Int,
    val totalArea:Int,
    val receptionPiecesNumber: Int,
    val kitchenNumber: Int,
    val bathroomNumber: Int,
    val bedroomNumber: Int,
    val forWhat:String,
    val sellPrice:Int?,
    val rentPrice:Int?,
    val rentDuration:String?
    ) : Parcelable
