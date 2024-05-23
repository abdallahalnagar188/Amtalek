package eramo.amtalek.domain.model.home.cities

import com.google.gson.annotations.SerializedName

data class CitiesModel (
    var createdAt: String,
    var id: Int,
    var image: String,
    var propertiesCount: Int,
    var rentProperties: Int,
    var salesProperties: Int,
    var title: String
    )