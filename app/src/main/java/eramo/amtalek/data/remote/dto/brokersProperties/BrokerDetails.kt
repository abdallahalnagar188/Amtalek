package eramo.amtalek.data.remote.dto.brokersProperties

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class BrokerDetails(

	@field:SerializedName("company_logo")
	val companyLogo: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("broker_type")
	val brokerType: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable