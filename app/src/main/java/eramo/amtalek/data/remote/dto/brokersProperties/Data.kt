package eramo.amtalek.data.remote.dto.brokersProperties

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Data(

	@field:SerializedName("exception")
	val exception: String? = null,

	@field:SerializedName("headers")
	val headers: Headers? = null,

	@field:SerializedName("original")
	val original: List<OriginalItem>? = null
) : Parcelable