package eramo.amtalek.data.remote.dto.myHome.news.allnews


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataX(
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