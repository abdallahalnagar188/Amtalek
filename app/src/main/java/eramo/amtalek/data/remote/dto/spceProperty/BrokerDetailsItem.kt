package eramo.amtalek.data.remote.dto.spceProperty

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class BrokerDetailsItem(

	@field:SerializedName("properties_count")
	val propertiesCount: Int? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("projects_count")
	val projectsCount: Int? = null,

	@field:SerializedName("broker_type")
	val brokerType: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("has_package")
	val hasPackage: String? = null
) : Parcelable