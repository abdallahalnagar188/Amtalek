package eramo.amtalek.data.remote.dto.myHome.news.newsDetails


import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.myHome.news.Title

data class NewsCategory(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_title")
    var mainTitle: String?,
    @SerializedName("title")
    var title: Title?
)