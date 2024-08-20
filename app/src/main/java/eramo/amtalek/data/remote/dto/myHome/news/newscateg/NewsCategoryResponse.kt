package eramo.amtalek.data.remote.dto.myHome.news.newscateg


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class NewsCategoryResponse(
    @SerializedName("data")
    var `data`: List<NewsCategoryData?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)

data class NewsCategoryData(
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("news")
    var news: News?,
    @SerializedName("title")
    var title: String?
)

data class News(
    @SerializedName("data")
    var `data`: List<NewsData?>?
)
@Parcelize
data class NewsData(
    @SerializedName("added_by")
    var addedBy: String?,
    @SerializedName("agent_info")
    var agentInfo: List<AgentInfo?>?,
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
): Parcelable
@Parcelize
data class AgentInfo(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("has_package")
    var hasPackage: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
//    @SerializedName("projects")
//    //var projects: List<Any?>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("property_for_rent")
    var propertyForRent: Int?,
    @SerializedName("property_for_sale")
    var propertyForSale: Int?
):Parcelable
@Parcelize
data class NewsCategory(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_title")
    var mainTitle: String?,
    @SerializedName("title")
    var title: Title?
):Parcelable
@Parcelize
data class Title(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
):Parcelable
