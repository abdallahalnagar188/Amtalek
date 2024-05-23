package eramo.amtalek.data.remote.dto.myHome.news


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.home.news.HomeNewsSectionsModel
import eramo.amtalek.domain.model.home.news.NewsModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeNewsResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class MyData(
    @SerializedName("added_by")
    var addedBy: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("love")
    var love: Int?,
    @SerializedName("news_category")
    var newsCategory: NewsCategory?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("views")
    var views: Int?
) : Parcelable{
    fun toNewsModel(): NewsModel {
        return NewsModel(
            addedBy = addedBy?:"",
            createdAt = createdAt?:"",
            description = description?:"",
            id = id?:0,
            image = image?:"",
            love = love?:0,
            newsCategory = newsCategory?:NewsCategory(0,"",Title("", "")),
            summary = summary?:"",
            title = title?:"",
            views = views?:0
        )
    }

}

@Parcelize
data class NewsCategory(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_title")
    var mainTitle: String?,
    @SerializedName("title")
    var title: Title?
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("data")
    var `data`: List<MyData>?,
    @SerializedName("title")
    var title: String?
) : Parcelable{
    fun toHomeNewsSectionModel():HomeNewsSectionsModel{
        return HomeNewsSectionsModel(
            title = title?:"",
            news = data?.map { it.toNewsModel() }
        )
    }
}

@Parcelize
data class Title(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
) : Parcelable
