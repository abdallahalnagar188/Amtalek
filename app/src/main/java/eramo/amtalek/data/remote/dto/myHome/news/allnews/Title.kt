package eramo.amtalek.data.remote.dto.myHome.news.allnews


import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
)