package eramo.amtalek.data.remote.dto.myHome.news.newsDetails


import com.google.gson.annotations.SerializedName

data class NewsDetailsResponseX(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)