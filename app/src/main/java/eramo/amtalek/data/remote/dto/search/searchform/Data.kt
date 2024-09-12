package eramo.amtalek.data.remote.dto.search.searchform


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("properties_types")
    var propertiesTypes: List<PropertiesType?>?,
    @SerializedName("total_props")
    var totalProps: Int?
)