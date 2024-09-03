package eramo.amtalek.data.remote.dto.property


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.ChartModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.util.FALSE
import eramo.amtalek.util.NONE_IMAGE_URL
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.enum.AdvertisementType

data class PropertyDetailsResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("address")
        val address: String?,
        @SerializedName("aminities")
        val aminities: List<Aminity?>?,
        @SerializedName("apartment_num")
        val apartmentNum: Int?,
        @SerializedName("bath_room_no")
        val bathRoomNo: Int?,
        @SerializedName("bed_rooms_no")
        val bedRoomsNo: Int?,
        @SerializedName("broker_details")
        val brokerDetails: List<BrokerDetail?>?,
        @SerializedName("building_num")
        val buildingNum: Int?,
        @SerializedName("category")
        val category: String?,
        @SerializedName("chart")
        val chart: List<Chart?>?,
        @SerializedName("city")
        val city: String?,
        @SerializedName("comments")
        val comments: List<Comment?>?,
        @SerializedName("country")
        val country: String?,
        @SerializedName("currency")
        val currency: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("facebook")
        val facebook: String?,
        @SerializedName("finishing")
        val finishing: String?,
        @SerializedName("floor_num")
        val floorNum: Int?,
        @SerializedName("for_what")
        val forWhat: String?,
        @SerializedName("garage_no")
        val garageNo: Int?,
        @SerializedName("garage_size")
        val garageSize: Int?,
        @SerializedName("google_plus")
        val googlePlus: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("kitchens_no")
        val kitchensNo: Int?,
        @SerializedName("land_area")
        val landArea: Int?,
        @SerializedName("listing_number")
        val listingNumber: String?,
        @SerializedName("living_room")
        val livingRoom: Int?,
        @SerializedName("location")
        val location: String?,
        @SerializedName("no_floors")
        val noFloors: Int?,
        @SerializedName("normal_featured")
        val normalFeatured: String?,
        @SerializedName("on_site")
        val onSite: String?,
        @SerializedName("primary_image")
        val primaryImage: String?,
        @SerializedName("property_type")
        val propertyType: String?,
        @SerializedName("purpose")
        val purpose: String?,
        @SerializedName("reception_floor_type")
        val receptionFloorType: String?,
        @SerializedName("reception_pieces")
        val receptionPieces: Int?,
        @SerializedName("region")
        val region: String?,
        @SerializedName("rent_duration")
        val rentDuration: String?,
        @SerializedName("rent_price")
        val rentPrice: Int?,
        @SerializedName("room_ensuite")
        val roomEnsuite: Int?,
        @SerializedName("sale_price")
        val salePrice: Int?,
        @SerializedName("similar_properties")
        val similarProperties: List<SimilarProperty?>?,
        @SerializedName("sliders")
        val sliders: List<Slider?>?,
        @SerializedName("summary")
        val summary: List<Summary?>?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("total_property_area")
        val totalPropertyArea: Int?,
        @SerializedName("twitter")
        val twitter: String?,
        @SerializedName("unit_floor")
        val unitFloor: String?,
        @SerializedName("video")
        val video: String?,
        @SerializedName("video_type")
        val videoType: String?,
        @SerializedName("views")
        val views: Int?
    ) {
        data class Aminity(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("image")
            val image: String?
        )

        data class BrokerDetail(
            @SerializedName("broker_type")
            val brokerType: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("logo")
            val logo: String?,
            @SerializedName("mobile")
            val mobile: Any?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("phone")
            val phone: String?,
            @SerializedName("properties_count")
            val propertiesCount: Int?
        )

        data class Chart(
            @SerializedName("count")
            val count: Int?,
            @SerializedName("date")
            val date: String?,
            @SerializedName("id")
            val id: Int?
        )

        data class Comment(
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("stars")
            val stars: String?,
            @SerializedName("user_comment")
            val userComment: String?,
            @SerializedName("user_image")
            val userImage: String?,
            @SerializedName("user_name")
            val userName: String?
        )

        data class SimilarProperty(
            @SerializedName("address")
            val address: String?,
            @SerializedName("bath_room_no")
            val bathRoomNo: Int?,
            @SerializedName("bed_rooms_no")
            val bedRoomsNo: Int?,
            @SerializedName("broker_details")
            val brokerDetails: List<Any?>?,
            @SerializedName("category")
            val category: String?,
            @SerializedName("city")
            val city: String?,
            @SerializedName("country")
            val country: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("currency")
            val currency: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("facebook")
            val facebook: String?,
            @SerializedName("for_what")
            val forWhat: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_fav")
            val isFav: String?,
            @SerializedName("land_area")
            val landArea: Int?,
            @SerializedName("listing_number")
            val listingNumber: String?,
            @SerializedName("normal_featured")
            val normalFeatured: String?,
            @SerializedName("primary_image")
            val primaryImage: String?,
            @SerializedName("priority")
            val priority: String?,
            @SerializedName("property_type")
            val propertyType: String?,
            @SerializedName("purpose")
            val purpose: String?,
            @SerializedName("region")
            val region: String?,
            @SerializedName("rent_duration")
            val rentDuration: String?,
            @SerializedName("rent_price")
            val rentPrice: Int?,
            @SerializedName("sale_price")
            val salePrice: Int?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("total_property_area")
            val totalPropertyArea: Int?
        )

        data class Slider(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("src")
            val src: String?
        )

        data class Summary(
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("key")
            val key: String?,
            @SerializedName("property_id")
            val propertyId: Int?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("value")
            val value: String?
        )
    }

    //----------------------------------------------------- Lists -----------------------------------------------------//
    private fun sliderImagesList(): List<String> {
        val list = mutableListOf<String>()
        for (i in data?.get(0)?.sliders!!) {
            list.add(i?.src ?: "")
        }
        return list
    }

    private fun propertyFeaturesList(): List<AmenityModel> {
        val list = mutableListOf<AmenityModel>()
        for (i in data?.get(0)?.aminities!!) {
            list.add(AmenityModel(name = i?.title?:"", id = i?.id?:-1, image = i?.image?:"" ))
        }
        return list
    }

    private fun commentsList(): List<RatingCommentsModel> {
        val list = mutableListOf<RatingCommentsModel>()

        for (i in data?.get(0)?.comments!!) {
            list.add(
                RatingCommentsModel(
                    i?.userName ?: "",
                    i?.userName ?: "",
                    i?.userImage ?: "",
                    i?.userComment ?: "",
                    i?.createdAt ?: "",
                    i?.stars?.toFloat() ?: 0.0f
                )
            )
        }

        return list
    }

    private fun similarPropertiesList(): List<PropertyModelx> {
        val list = mutableListOf<PropertyModelx>()

        for (i in data?.get(0)?.similarProperties!!) {
            list.add(
                PropertyModelx(
                    i?.id ?: -1,
                    i?.primaryImage ?: "",
                    i?.forWhat ?: "",
                    if (i?.isFav == "1") TRUE else FALSE,
                    if (i?.normalFeatured == AdvertisementType.FEATURED.key) TRUE else FALSE,
                    i?.salePrice?.toDouble() ?: 0.0,
                    i?.rentPrice?.toDouble() ?: 0.0,
                    i?.rentDuration ?: "",
                    i?.title ?: "",
                    i?.currency?:"",
                    i?.landArea ?: 0,
                    i?.bathRoomNo ?: 0,
                    i?.bedRoomsNo ?: 0,
                    "${i?.region}, ${i?.city}",
                    i?.createdAt ?: "",
                    NONE_IMAGE_URL
                )
            )
        }

        return list
    }

    private fun chartList(): List<ChartModel> {
        val list = mutableListOf<ChartModel>()

//        for (i in data?.get(0)?.chart!!) {
//            list.add(
//                ChartModel(
//                    i?.id ?: -1, i?.count ?: -1, i?.date ?: ""
//                )
//            )
//        }

        return list
    }

//    fun toPropertyDetailsModel(): PropertyDetailsModel {
//        return PropertyDetailsModel(
//            sliderImagesList(),
//            data?.get(0)?.salePrice?.toDouble() ?: 0.0,
//            data?.get(0)?.rentPrice?.toDouble() ?: 0.0,
//            data?.get(0)?.currency?:"",
//            data?.get(0)?.rentDuration ?: "",
//            data?.get(0)?.title ?: "",
//            "${data?.get(0)?.region}, ${data?.get(0)?.city}",
//            "----",
//            data?.get(0)?.finishing ?: "",
//            "----",
//            data?.get(0)?.region ?: "",
//            data?.get(0)?.landArea ?: -1,
//            data?.get(0)?.bathRoomNo ?: -1,
//            data?.get(0)?.bedRoomsNo ?: -1,
//            data?.get(0)?.brokerDetails?.get(0)?.logo ?: "",
//            data?.get(0)?.brokerDetails?.get(0)?.name ?: "",
//            data?.get(0)?.brokerDetails?.get(0)?.description ?: "",
//            data?.get(0)?.listingNumber ?: "",
//            data?.get(0)?.propertyType ?: "",
//            "----",
//            "----",
//            listOf(data?.get(0)?.floorNum ?: -1),
//            data?.get(0)?.receptionFloorType ?: "",
//            data?.get(0)?.description ?: "",
//            propertyFeaturesList(),
//            data?.get(0)?.video ?: "",
//            commentsList(),
//            similarPropertiesList(),
//            chartList(),
//            mapUrl ="",
//            "",
//        )
//    }
}