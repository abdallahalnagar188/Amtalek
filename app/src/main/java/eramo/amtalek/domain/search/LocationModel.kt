package eramo.amtalek.domain.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    var id: Int,
    var propertiesCount: Int,
    var title: String
): Parcelable