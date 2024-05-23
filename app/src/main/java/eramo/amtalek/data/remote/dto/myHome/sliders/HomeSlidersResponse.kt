package eramo.amtalek.data.remote.dto.myHome.sliders


import android.os.Parcelable
import android.transition.Slide
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.home.slider.SliderModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeSlidersResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?
) : Parcelable{
    fun toSliderModel(): SliderModel {
        return SliderModel(
            image = image?:"",
            id = id?:0
        )
    }
}