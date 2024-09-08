package eramo.amtalek.data.remote.dto.myHome.news.newsDetails


import com.google.gson.annotations.SerializedName

data class CategoryDetails(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?
)