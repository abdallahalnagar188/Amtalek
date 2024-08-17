package eramo.amtalek.data.remote.dto.myHome.news.allnews


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsCategory(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_title")
    var mainTitle: String?,
    @SerializedName("title")
    var title: Title?
): Parcelable