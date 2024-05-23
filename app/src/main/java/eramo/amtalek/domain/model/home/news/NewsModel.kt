package eramo.amtalek.domain.model.home.news

import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.myHome.news.NewsCategory

data class NewsModel (
    var addedBy: String,
    var createdAt: String,
    var description: String,
    var id: Int,
    var image: String,
    var love: Int,
    var newsCategory: NewsCategory,
    var summary: String,
    var title: String,
    var views: Int?
)