package eramo.amtalek.domain.search

import com.google.gson.annotations.SerializedName

data class LocationModel(
    var id: Int,
    var propertiesCount: Int,
    var title: String
)