package eramo.amtalek.data.remote.dto.myHome.news.newsDetails


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("added_by")
    var addedBy: String?,
    @SerializedName("agent_info")
    var agentInfo: List<AgentInfo>?,
    @SerializedName("category_details")
    var categoryDetails: CategoryDetails?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facebook")
    var facebook: String?,
    @SerializedName("google_plus")
    var googlePlus: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("latest_news")
    var latestNews: List<LatestNew>?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("twitter")
    var twitter: String?,
    @SerializedName("views")
    var views: Int?
)