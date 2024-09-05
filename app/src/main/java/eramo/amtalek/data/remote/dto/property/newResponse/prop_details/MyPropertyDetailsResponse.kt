package eramo.amtalek.data.remote.dto.property.newResponse.prop_details


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.project.AutocadModel
import eramo.amtalek.domain.model.property.ChartModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.util.NONE_IMAGE_URL
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyPropertyDetailsResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable {
    fun toPropertyDetailsModel(): PropertyDetailsModel {
        return PropertyDetailsModel(
            sliderImages = sliderImagesList(),
            sellPrice = data?.get(0)?.salePrice?.toDouble() ?: 0.0,
            rentPrice = data?.get(0)?.rentPrice?.toDouble() ?: 0.0,
            currency = data?.get(0)?.currency ?: "",
            rentDuration = data?.get(0)?.rentDuration ?: "",
            title = data?.get(0)?.title ?: "",
            location = "${data?.get(0)?.city}, ${data?.get(0)?.region}, ${data?.get(0)?.subRegion}",
            datePosted = data?.get(0)?.createdAt ?: "",
            finishing = data?.get(0)?.finishing ?: "",
            finishingAvailability = "----",
            areaLocation = data?.get(0)?.region ?: "",
            area = data?.get(0)?.landArea ?: -1,
            bathroomsCount = data?.get(0)?.bathRoomNo ?: -1,
            bedroomsCount = data?.get(0)?.bedRoomsNo ?: -1,
            brokerImageUrl = data?.get(0)?.brokerDetails?.get(0)?.logo ?: "",
            brokerName = data?.get(0)?.brokerDetails?.get(0)?.name ?: "",
            brokerDescription = data?.get(0)?.brokerDetails?.get(0)?.description ?: "",
            propertyCode = data?.get(0)?.listingNumber ?: "",
            propertyType = data?.get(0)?.propertyType ?: "",
            furniture = "----",
            forWhat = data?.get(0)?.forWhat ?: "",
            floors = listOf(data?.get(0)?.floorNum ?: -1),
            landType = data?.get(0)?.receptionFloorType ?: "",
            description = data?.get(0)?.description ?: "",
            propertyAmenities = propertyFeaturesList(),
            videoUrl = data?.get(0)?.video ?: "",
            comments = commentsList(),
            similarProperties = similarPropertiesList(),
            chartList = chartList(),
            mapUrl = data?.get(0)?.location ?: "",
            payment = "---",
            id = data?.get(0)?.id ?: -1,
            isFavourite = data?.get(0)?.isFav ?: "",
            sold = data?.get(0)?.sold ?: false,
            roi = data?.get(0)?.roi ?: "",
            calcRoi = data?.get(0)?.calcRoi ?: "",
            brokerId = data?.get(0)?.brokerDetails?.get(0)?.id ?: -1,
            brokerEmail = data?.get(0)?.brokerDetails?.get(0)?.email ?: "",
            brokerPhone = data?.get(0)?.brokerDetails?.get(0)?.phone ?: "",
            vendorType = data?.get(0)?.brokerDetails?.get(0)?.brokerType ?: "",
            autocad = propertyAutocadList(),
            facebook = data?.get(0)?.facebook ?: "",
            twitter = data?.get(0)?.twitter ?: "",
            listingNumber = data?.get(0)?.listingNumber ?: ""
            )
    }

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
            list.add(AmenityModel(name = i.title ?: "", id = i.id ?: -1, image = i.image))
        }
        return list
    }
    private fun propertyAutocadList(): List<AutocadModel> {
        val list = mutableListOf<AutocadModel>()
        for (i in data?.get(0)?.autocad!!) {
            list.add(AutocadModel(id = i?.id ?: -1, src = i?.src ?: "", type = i?.type ?: ""))
        }
        return list
    }

    private fun commentsList(): List<RatingCommentsModel> {
        val list = mutableListOf<RatingCommentsModel>()

        for (i in data?.get(0)?.comments!!) {
            list.add(
                RatingCommentsModel(
                    i.userName ?: "",
                    i.userName ?: "",
                    i.userImage ?: "",
                    i.userComment ?: "",
                    i.createdAt ?: "",
                    i.stars?.toFloat() ?: 0.0f
                )
            )
        }

        return list
    }

    private fun similarPropertiesList(): List<PropertyModel> {
        val list = mutableListOf<PropertyModel>()
        for (i in data?.get(0)?.similarProperties!!) {
            list.add(
                PropertyModel(
                    id = i?.id ?: -1,
                    imageUrl = i?.primaryImage ?: "",
                    type = i?.forWhat ?: "",
                    isFavourite = i?.isFav ?: "",
                    isFeatured = i?.priority ?: "",
                    sellPrice = i?.salePrice ?: 0,
                    rentPrice = i?.rentPrice ?: 0,
                    listingNumber = i?.listingNumber ?: "",
                    rentDuration = i?.rentDuration ?: "",
                    title = i?.title ?: "",
                    area = i?.landArea ?: 0,
                    bathroomsCount = i?.bathRoomNo ?: 0,
                    bedsCount = i?.bedRoomsNo ?: 0,
                    location = "${i?.city}, ${i?.region}, ${i?.subRegion}",
                    datePosted = i?.createdAt ?: "",
                    brokerId = i.brokerDetails?.id.toString() ?: "",
                    brokerLogoUrl = i.brokerDetails?.company_logo ?: NONE_IMAGE_URL,
                    currency = i.currency ?: "",
                    acceptance = i.acceptance ?: "",
                    sold = i.sold ?: false,
                    offerData = null,
                    region = i.region ?: "",
                    subRegion = i.subRegion ?: "",
                    vendorType = i.brokerDetails?.broker_type ?: "",
                    senderName = i.brokerDetails?.company_name ?: "",
                    senderPhone = "",
                    offerPrice = "",
                    offerDate = "",
                )
            )
        }

        return list
    }

    private fun chartList(): List<ChartModel> {
        val list = mutableListOf<ChartModel>()

        for (i in data?.get(0)?.chart!!) {
            list.add(
                ChartModel(
                    i ?: -1
                )
            )
        }

        return list
    }


}

@Parcelize
data class Data(
    @SerializedName("address")
    var address: String?,
    @SerializedName("aminities")
    var aminities: List<Aminity>?,
    @SerializedName("apartment_num")
    var apartmentNum: Int?,
    @SerializedName("autocad")
    var autocad: List<Autocad>?,
    @SerializedName("summary")
    var summary: List<summaryItem>?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("broker_details")
    var brokerDetails: List<BrokerDetail>?,
    @SerializedName("building_num")
    var buildingNum: Int?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("chart")
    var chart: List<Int>?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("comments")
    var comments: List<Comment>?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facebook")
    var facebook: String?,
    @SerializedName("finishing")
    var finishing: String?,
    @SerializedName("floor_num")
    var floorNum: Int?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("garage_no")
    var garageNo: Int?,
    @SerializedName("garage_size")
    var garageSize: Int?,
    @SerializedName("google_plus")
    var googlePlus: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("images_count")
    var imagesCount: Int?,
    @SerializedName("kitchens_no")
    var kitchensNo: Int?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("living_room")
    var livingRoom: Int?,
    @SerializedName("location")
    var location: String?,
    @SerializedName("no_floors")
    var noFloors: Int?,
    @SerializedName("normal_featured")
    var normalFeatured: String?,
    @SerializedName("on_site")
    var onSite: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("reception_floor_type")
    var receptionFloorType: String?,
    @SerializedName("reception_pieces")
    var receptionPieces: Int?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("room_ensuite")
    var roomEnsuite: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("similar_properties")
    var similarProperties: List<SimilarProperty>?,
    @SerializedName("sliders")
    var sliders: List<Slider>?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("total_property_area")
    var totalPropertyArea: Int?,
    @SerializedName("twitter")
    var twitter: String?,
    @SerializedName("unit_floor")
    var unitFloor: String?,
    @SerializedName("video")
    var video: String?,
    @SerializedName("video_type")
    var videoType: String?,
    @SerializedName("views")
    var views: Int?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("calc_roi")
    var calcRoi: String?,
    @SerializedName("roi")
    var roi: String?,
    @SerializedName("offer_status")
    var offerStatus: Boolean?,
    @SerializedName("sub_region")
    var subRegion: String?,

    ) : Parcelable

@Parcelize
data class Slider(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("src")
    var src: String?,
    @SerializedName("type")
    var type: String?
) : Parcelable

@Parcelize
data class SimilarProperty(
    @SerializedName("address")
    var address: String?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("broker_details")
    var brokerDetails: BrokerDetailX?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facebook")
    var facebook: String?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("images_count")
    var imagesCount: Int?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("total_property_area")
    var totalPropertyArea: Int?,
    @SerializedName("acceptance")
    var acceptance: String?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("offer_status")
    var offerStatus: Boolean?,
    @SerializedName("sub_region")
    var subRegion: String?,

    ) : Parcelable

@Parcelize
data class Project(
    @SerializedName("agent_data")
    var agentData: AgentData?,
    @SerializedName("bedrooms")
    var bedrooms: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("delivery_date")
    var deliveryDate: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facebook")
    var facebook: String?,
    @SerializedName("google_plus")
    var googlePlus: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("price_from")
    var priceFrom: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("total_units")
    var totalUnits: String?,
    @SerializedName("twitter")
    var twitter: String?
) : Parcelable

@Parcelize
data class Comment(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("stars")
    var stars: String?,
    @SerializedName("user_comment")
    var userComment: String?,
    @SerializedName("user_image")
    var userImage: String?,
    @SerializedName("user_name")
    var userName: String?
) : Parcelable

@Parcelize
data class BrokerDetailX(
    @SerializedName("company_name")
    var company_name: String?,
    @SerializedName("company_logo")
    var company_logo: String?,
    @SerializedName("broker_type")
    var broker_type: String?,
    @SerializedName("id")
    var id: Int?,
) : Parcelable

@Parcelize
data class BrokerDetail(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("projects")
    var projects: List<Project?>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
) : Parcelable

@Parcelize
data class Autocad(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("src")
    var src: String?,
    @SerializedName("type")
    var type: String?
) : Parcelable{
    fun toAutocadModel():AutocadModel{
        return AutocadModel(id = id?:-1, src = src?:"", type = type?:"")
    }
}

@Parcelize
data class summaryItem(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("key")
    var key: String?,
    @SerializedName("property_id")
    var propertyId: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("value")
    var value: String?
) : Parcelable

@Parcelize
data class Aminity(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("image")
    var image: String?
) : Parcelable

@Parcelize
data class AgentData(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
) : Parcelable