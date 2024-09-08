package eramo.amtalek.domain.model.home.news

import android.os.Parcelable
import eramo.amtalek.data.remote.dto.myHome.news.NewsCategory
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable