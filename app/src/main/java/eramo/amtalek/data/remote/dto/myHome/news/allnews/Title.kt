package eramo.amtalek.data.remote.dto.myHome.news.allnews


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Title(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
): Parcelable