package eramo.amtalek.data.remote.dto.spceProperty

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SlidersItem(

	@field:SerializedName("src")
	val src: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable