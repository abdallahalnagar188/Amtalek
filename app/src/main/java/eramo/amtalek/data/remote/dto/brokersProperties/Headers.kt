package eramo.amtalek.data.remote.dto.brokersProperties

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class Headers(
	val any: String? = null
) : Parcelable