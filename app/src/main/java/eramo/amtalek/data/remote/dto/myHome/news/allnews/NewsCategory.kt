package eramo.amtalek.data.remote.dto.myHome.news.allnews


import com.google.gson.annotations.SerializedName

data class NewsCategory(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_title")
    var mainTitle: String?,
    @SerializedName("title")
    var title: Title?
)