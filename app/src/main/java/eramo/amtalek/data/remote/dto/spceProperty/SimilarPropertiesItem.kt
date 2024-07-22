package eramo.amtalek.data.remote.dto.spceProperty

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SimilarPropertiesItem(

	@field:SerializedName("primary_image")
	val primaryImage: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("listing_number")
	val listingNumber: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("land_area")
	val landArea: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("bath_room_no")
	val bathRoomNo: Int? = null,

	@field:SerializedName("property_type")
	val propertyType: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sold")
	val sold: Boolean? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("broker_details")
	val brokerDetails: BrokerDetails? = null,

	@field:SerializedName("is_fav")
	val isFav: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("bed_rooms_no")
	val bedRoomsNo: Int? = null,

	@field:SerializedName("sale_price")
	val salePrice: Int? = null,

	@field:SerializedName("acceptance")
	val acceptance: String? = null,

	@field:SerializedName("for_what")
	val forWhat: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("region")
	val region: String? = null,

	@field:SerializedName("sub_region")
	val subRegion: String? = null,

	@field:SerializedName("rent_duration")
	val rentDuration: String? = null
) : Parcelable