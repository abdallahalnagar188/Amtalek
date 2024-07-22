package eramo.amtalek.data.remote.dto.spceProperty

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DataItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("rent_price")
	val rentPrice: Int? = null,

	@field:SerializedName("no_floors")
	val noFloors: Int? = null,

	@field:SerializedName("building_num")
	val buildingNum: Int? = null,

	@field:SerializedName("twitter")
	val twitter: String? = null,

	@field:SerializedName("living_room")
	val livingRoom: Int? = null,

	@field:SerializedName("garage_size")
	val garageSize: Int? = null,

	@field:SerializedName("garage_no")
	val garageNo: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sliders")
	val sliders: List<SlidersItem?>? = null,

	@field:SerializedName("floor_num")
	val floorNum: Int? = null,

	@field:SerializedName("images_count")
	val imagesCount: Int? = null,

	@field:SerializedName("is_fav")
	val isFav: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("total_property_area")
	val totalPropertyArea: Int? = null,

	@field:SerializedName("region")
	val region: String? = null,

	@field:SerializedName("primary_image")
	val primaryImage: String? = null,

	@field:SerializedName("listing_number")
	val listingNumber: String? = null,

	@field:SerializedName("kitchens_no")
	val kitchensNo: Int? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("land_area")
	val landArea: Int? = null,

	@field:SerializedName("video")
	val video: String? = null,

	@field:SerializedName("similar_properties")
	val similarProperties: List<SimilarPropertiesItem?>? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("autocad")
	val autocad: List<String?>? = null,

	@field:SerializedName("roi")
	val roi: String? = null,

	@field:SerializedName("apartment_num")
	val apartmentNum: Int? = null,

	@field:SerializedName("finishing")
	val finishing: String? = null,

	@field:SerializedName("bath_room_no")
	val bathRoomNo: Int? = null,

	@field:SerializedName("property_type")
	val propertyType: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("reception_pieces")
	val receptionPieces: Int? = null,

	@field:SerializedName("on_site")
	val onSite: String? = null,

	@field:SerializedName("views")
	val views: Int? = null,

	@field:SerializedName("calc_roi")
	val calcRoi: String? = null,

	@field:SerializedName("summary")
	val summary: List<String?>? = null,

	@field:SerializedName("sold")
	val sold: Boolean? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("comments")
	val comments: List<String?>? = null,

	@field:SerializedName("broker_details")
	val brokerDetails: List<BrokerDetailsItem?>? = null,

	@field:SerializedName("facebook")
	val facebook: String? = null,

	@field:SerializedName("google_plus")
	val googlePlus: String? = null,

	@field:SerializedName("bed_rooms_no")
	val bedRoomsNo: Int? = null,

	@field:SerializedName("sale_price")
	val salePrice: Int? = null,

	@field:SerializedName("room_ensuite")
	val roomEnsuite: Int? = null,

	@field:SerializedName("video_type")
	val videoType: String? = null,

	@field:SerializedName("aminities")
	val aminities: List<AminitiesItem?>? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("for_what")
	val forWhat: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("sub_region")
	val subRegion: String? = null,

	@field:SerializedName("chart")
	val chart: List<Int?>? = null,

	@field:SerializedName("rent_duration")
	val rentDuration: String? = null,

	@field:SerializedName("reception_floor_type")
	val receptionFloorType: String? = null
) : Parcelable