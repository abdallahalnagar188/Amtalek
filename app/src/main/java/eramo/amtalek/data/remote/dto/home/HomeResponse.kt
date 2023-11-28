package eramo.amtalek.data.remote.dto.home


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.main.home.ProjectHomeModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.util.FALSE
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.enum.AdvertisementType

data class HomeResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("adds")
        val adds: List<Adds?>?,
        @SerializedName("appaerments")
        val appaerments: List<Appaerment?>?,
        @SerializedName("duplixes")
        val duplixes: List<Duplixe?>?,
        @SerializedName("featured_projects_city")
        val featuredProjectsCity: List<FeaturedProjectsCity?>?,
        @SerializedName("featured_properties_country")
        val featuredPropertiesCountry: List<FeaturedPropertiesCountry?>?,
        @SerializedName("news")
        val news: List<New?>?,
        @SerializedName("property_in_city")
        val propertyInCity: List<PropertyInCity?>?,
        @SerializedName("sliders")
        val sliders: List<Slider?>?,
        @SerializedName("villas")
        val villas: List<Villa?>?
    ) {

        data class Adds(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("link")
            val link: String?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("sub_title")
            val sub_title: String?,
        )

        data class Appaerment(
            @SerializedName("address")
            val address: String?,
            @SerializedName("bath_room_no")
            val bathRoomNo: Int?,
            @SerializedName("bed_rooms_no")
            val bedRoomsNo: Int?,
            @SerializedName("broker_details")
            val brokerDetails: List<BrokerDetail?>?,
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
            @SerializedName("for_what")
            val forWhat: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_fav")
            val isFav: String?,
            @SerializedName("land_area")
            val landArea: Int?,
            @SerializedName("normal_featured")
            val normalFeatured: String?,
            @SerializedName("primary_image")
            val primaryImage: String?,
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
            val title: String?
        ) {
            data class BrokerDetail(
                @SerializedName("broker_type")
                val brokerType: String?,
                @SerializedName("description")
                val description: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("first_name")
                val firstName: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("last_name")
                val lastName: String?,
                @SerializedName("logo")
                val logo: String?,
                @SerializedName("mobile")
                val mobile: Any?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("offers")
                val offers: List<Offer?>?,
                @SerializedName("phone")
                val phone: String?,
                @SerializedName("properties_count")
                val propertiesCount: Int?,
                @SerializedName("src")
                val src: String?,
                @SerializedName("submitted_props")
                val submittedProps: List<SubmittedProp?>?
            ) {
                data class Offer(
                    @SerializedName("created_at")
                    val createdAt: String?,
                    @SerializedName("email")
                    val email: String?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("offer")
                    val offer: String?,
                    @SerializedName("offer_type")
                    val offerType: String?,
                    @SerializedName("phone")
                    val phone: String?,
                    @SerializedName("property")
                    val `property`: Property?,
                    @SerializedName("property_id")
                    val propertyId: Int?,
                    @SerializedName("seen")
                    val seen: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?
                ) {
                    data class Property(
                        @SerializedName("acceptance")
                        val acceptance: String?,
                        @SerializedName("added_by")
                        val addedBy: Int?,
                        @SerializedName("adding_type")
                        val addingType: String?,
                        @SerializedName("address")
                        val address: Address?,
                        @SerializedName("apartment_num")
                        val apartmentNum: Int?,
                        @SerializedName("bath_room_no")
                        val bathRoomNo: Int?,
                        @SerializedName("bed_rooms_no")
                        val bedRoomsNo: Int?,
                        @SerializedName("building_num")
                        val buildingNum: Int?,
                        @SerializedName("category")
                        val category: Int?,
                        @SerializedName("city")
                        val city: Int?,
                        @SerializedName("country")
                        val country: Int?,
                        @SerializedName("created_at")
                        val createdAt: String?,
                        @SerializedName("currency")
                        val currency: Int?,
                        @SerializedName("deleted_at")
                        val deletedAt: Any?,
                        @SerializedName("description")
                        val description: Description?,
                        @SerializedName("finishing")
                        val finishing: Int?,
                        @SerializedName("floor_num")
                        val floorNum: Int?,
                        @SerializedName("for_what")
                        val forWhat: String?,
                        @SerializedName("garage_no")
                        val garageNo: Int?,
                        @SerializedName("garage_size")
                        val garageSize: Int?,
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
                        @SerializedName("on_site")
                        val onSite: String?,
                        @SerializedName("primary_image")
                        val primaryImage: String?,
                        @SerializedName("priority")
                        val priority: String?,
                        @SerializedName("property_type")
                        val propertyType: Int?,
                        @SerializedName("purpose")
                        val purpose: Int?,
                        @SerializedName("reception_floor_type")
                        val receptionFloorType: Int?,
                        @SerializedName("reception_pieces")
                        val receptionPieces: Int?,
                        @SerializedName("region")
                        val region: Int?,
                        @SerializedName("rent_duration")
                        val rentDuration: String?,
                        @SerializedName("rent_price")
                        val rentPrice: Int?,
                        @SerializedName("room_ensuite")
                        val roomEnsuite: Int?,
                        @SerializedName("sale_price")
                        val salePrice: Int?,
                        @SerializedName("title")
                        val title: Title?,
                        @SerializedName("total_area")
                        val totalArea: Int?,
                        @SerializedName("unit_floor")
                        val unitFloor: String?,
                        @SerializedName("updated_at")
                        val updatedAt: String?,
                        @SerializedName("video")
                        val video: String?,
                        @SerializedName("video_type")
                        val videoType: String?,
                        @SerializedName("views")
                        val views: Int?
                    ) {
                        data class Address(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )

                        data class Description(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )

                        data class Title(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )
                    }
                }

                data class SubmittedProp(
                    @SerializedName("acceptance")
                    val acceptance: String?,
                    @SerializedName("added_by")
                    val addedBy: Int?,
                    @SerializedName("adding_type")
                    val addingType: String?,
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("apartment_num")
                    val apartmentNum: Int?,
                    @SerializedName("bath_room_no")
                    val bathRoomNo: Int?,
                    @SerializedName("bed_rooms_no")
                    val bedRoomsNo: Int?,
                    @SerializedName("building_num")
                    val buildingNum: Int?,
                    @SerializedName("category")
                    val category: Int?,
                    @SerializedName("city")
                    val city: Int?,
                    @SerializedName("country")
                    val country: Int?,
                    @SerializedName("created_at")
                    val createdAt: String?,
                    @SerializedName("currency")
                    val currency: Int?,
                    @SerializedName("deleted_at")
                    val deletedAt: Any?,
                    @SerializedName("description")
                    val description: Description?,
                    @SerializedName("finishing")
                    val finishing: Int?,
                    @SerializedName("floor_num")
                    val floorNum: Int?,
                    @SerializedName("for_what")
                    val forWhat: String?,
                    @SerializedName("garage_no")
                    val garageNo: Int?,
                    @SerializedName("garage_size")
                    val garageSize: Int?,
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
                    @SerializedName("on_site")
                    val onSite: String?,
                    @SerializedName("primary_image")
                    val primaryImage: String?,
                    @SerializedName("priority")
                    val priority: String?,
                    @SerializedName("property_type")
                    val propertyType: Int?,
                    @SerializedName("purpose")
                    val purpose: Int?,
                    @SerializedName("reception_floor_type")
                    val receptionFloorType: Int?,
                    @SerializedName("reception_pieces")
                    val receptionPieces: Int?,
                    @SerializedName("region")
                    val region: Int?,
                    @SerializedName("rent_duration")
                    val rentDuration: String?,
                    @SerializedName("rent_price")
                    val rentPrice: Int?,
                    @SerializedName("room_ensuite")
                    val roomEnsuite: Int?,
                    @SerializedName("sale_price")
                    val salePrice: Int?,
                    @SerializedName("title")
                    val title: Title?,
                    @SerializedName("total_area")
                    val totalArea: Int?,
                    @SerializedName("unit_floor")
                    val unitFloor: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?,
                    @SerializedName("video")
                    val video: String?,
                    @SerializedName("video_type")
                    val videoType: String?,
                    @SerializedName("views")
                    val views: Int?
                ) {
                    data class Address(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )

                    data class Description(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )

                    data class Title(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )
                }
            }

            fun toPropertyModel(): PropertyModel {
                return PropertyModel(
                    id ?: -1,
                    primaryImage ?: "",
                    forWhat ?: "",
                    if (isFav == "1") TRUE else FALSE,
                    if (normalFeatured == AdvertisementType.FEATURED.key) TRUE else FALSE,
                    salePrice?.toDouble() ?: 0.0,
                    rentPrice?.toDouble() ?: 0.0,
                    rentDuration ?: "",
                    title ?: "",
                    landArea ?: 0,
                    bathRoomNo ?: 0,
                    bedRoomsNo ?: 0,
                    "$region, $city",
                    createdAt ?: "",
                    brokerDetails?.get(0)?.logo ?: ""
                )
            }
        }

        data class Duplixe(
            @SerializedName("address")
            val address: String?,
            @SerializedName("bath_room_no")
            val bathRoomNo: Int?,
            @SerializedName("bed_rooms_no")
            val bedRoomsNo: Int?,
            @SerializedName("broker_details")
            val brokerDetails: List<BrokerDetail?>?,
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
            @SerializedName("for_what")
            val forWhat: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_fav")
            val isFav: String?,
            @SerializedName("land_area")
            val landArea: Int?,
            @SerializedName("normal_featured")
            val normalFeatured: String?,
            @SerializedName("primary_image")
            val primaryImage: String?,
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
            val title: String?
        ) {
            data class BrokerDetail(
                @SerializedName("broker_type")
                val brokerType: String?,
                @SerializedName("description")
                val description: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("first_name")
                val firstName: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("last_name")
                val lastName: String?,
                @SerializedName("logo")
                val logo: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("offers")
                val offers: List<Offer?>?,
                @SerializedName("phone")
                val phone: String?,
                @SerializedName("src")
                val src: String?,
                @SerializedName("submitted_props")
                val submittedProps: List<SubmittedProp?>?
            ) {
                data class Offer(
                    @SerializedName("created_at")
                    val createdAt: String?,
                    @SerializedName("email")
                    val email: String?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("offer")
                    val offer: String?,
                    @SerializedName("offer_type")
                    val offerType: String?,
                    @SerializedName("phone")
                    val phone: String?,
                    @SerializedName("property")
                    val `property`: Property?,
                    @SerializedName("property_id")
                    val propertyId: Int?,
                    @SerializedName("seen")
                    val seen: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?
                ) {
                    data class Property(
                        @SerializedName("acceptance")
                        val acceptance: String?,
                        @SerializedName("added_by")
                        val addedBy: Int?,
                        @SerializedName("adding_type")
                        val addingType: String?,
                        @SerializedName("address")
                        val address: Address?,
                        @SerializedName("apartment_num")
                        val apartmentNum: Int?,
                        @SerializedName("bath_room_no")
                        val bathRoomNo: Int?,
                        @SerializedName("bed_rooms_no")
                        val bedRoomsNo: Int?,
                        @SerializedName("building_num")
                        val buildingNum: Int?,
                        @SerializedName("category")
                        val category: Int?,
                        @SerializedName("city")
                        val city: Int?,
                        @SerializedName("country")
                        val country: Int?,
                        @SerializedName("created_at")
                        val createdAt: String?,
                        @SerializedName("currency")
                        val currency: Int?,
                        @SerializedName("deleted_at")
                        val deletedAt: Any?,
                        @SerializedName("description")
                        val description: Description?,
                        @SerializedName("finishing")
                        val finishing: Int?,
                        @SerializedName("floor_num")
                        val floorNum: Int?,
                        @SerializedName("for_what")
                        val forWhat: String?,
                        @SerializedName("garage_no")
                        val garageNo: Int?,
                        @SerializedName("garage_size")
                        val garageSize: Int?,
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
                        @SerializedName("on_site")
                        val onSite: String?,
                        @SerializedName("primary_image")
                        val primaryImage: String?,
                        @SerializedName("priority")
                        val priority: String?,
                        @SerializedName("property_type")
                        val propertyType: Int?,
                        @SerializedName("purpose")
                        val purpose: Int?,
                        @SerializedName("reception_floor_type")
                        val receptionFloorType: Int?,
                        @SerializedName("reception_pieces")
                        val receptionPieces: Int?,
                        @SerializedName("region")
                        val region: Int?,
                        @SerializedName("rent_duration")
                        val rentDuration: String?,
                        @SerializedName("rent_price")
                        val rentPrice: Int?,
                        @SerializedName("room_ensuite")
                        val roomEnsuite: Int?,
                        @SerializedName("sale_price")
                        val salePrice: Int?,
                        @SerializedName("title")
                        val title: Title?,
                        @SerializedName("total_area")
                        val totalArea: Int?,
                        @SerializedName("unit_floor")
                        val unitFloor: String?,
                        @SerializedName("updated_at")
                        val updatedAt: String?,
                        @SerializedName("video")
                        val video: String?,
                        @SerializedName("video_type")
                        val videoType: String?,
                        @SerializedName("views")
                        val views: Int?
                    ) {
                        data class Address(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )

                        data class Description(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )

                        data class Title(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )
                    }
                }

                data class SubmittedProp(
                    @SerializedName("acceptance")
                    val acceptance: String?,
                    @SerializedName("added_by")
                    val addedBy: Int?,
                    @SerializedName("adding_type")
                    val addingType: String?,
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("apartment_num")
                    val apartmentNum: Int?,
                    @SerializedName("bath_room_no")
                    val bathRoomNo: Int?,
                    @SerializedName("bed_rooms_no")
                    val bedRoomsNo: Int?,
                    @SerializedName("building_num")
                    val buildingNum: Int?,
                    @SerializedName("category")
                    val category: Int?,
                    @SerializedName("city")
                    val city: Int?,
                    @SerializedName("country")
                    val country: Int?,
                    @SerializedName("created_at")
                    val createdAt: String?,
                    @SerializedName("currency")
                    val currency: Int?,
                    @SerializedName("deleted_at")
                    val deletedAt: Any?,
                    @SerializedName("description")
                    val description: Description?,
                    @SerializedName("finishing")
                    val finishing: Int?,
                    @SerializedName("floor_num")
                    val floorNum: Int?,
                    @SerializedName("for_what")
                    val forWhat: String?,
                    @SerializedName("garage_no")
                    val garageNo: Int?,
                    @SerializedName("garage_size")
                    val garageSize: Int?,
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
                    @SerializedName("on_site")
                    val onSite: String?,
                    @SerializedName("primary_image")
                    val primaryImage: String?,
                    @SerializedName("priority")
                    val priority: String?,
                    @SerializedName("property_type")
                    val propertyType: Int?,
                    @SerializedName("purpose")
                    val purpose: Int?,
                    @SerializedName("reception_floor_type")
                    val receptionFloorType: Int?,
                    @SerializedName("reception_pieces")
                    val receptionPieces: Int?,
                    @SerializedName("region")
                    val region: Int?,
                    @SerializedName("rent_duration")
                    val rentDuration: String?,
                    @SerializedName("rent_price")
                    val rentPrice: Int?,
                    @SerializedName("room_ensuite")
                    val roomEnsuite: Int?,
                    @SerializedName("sale_price")
                    val salePrice: Int?,
                    @SerializedName("title")
                    val title: Title?,
                    @SerializedName("total_area")
                    val totalArea: Int?,
                    @SerializedName("unit_floor")
                    val unitFloor: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?,
                    @SerializedName("video")
                    val video: String?,
                    @SerializedName("video_type")
                    val videoType: String?,
                    @SerializedName("views")
                    val views: Int?
                ) {
                    data class Address(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )

                    data class Description(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )

                    data class Title(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )
                }
            }
        }

        data class FeaturedProjectsCity(
            @SerializedName("address")
            val address: String?,
            @SerializedName("broker_details")
            val brokerDetails: List<BrokerDetail?>?,
            @SerializedName("city")
            val city: String?,
            @SerializedName("country")
            val country: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_fav")
            val isFav: String?,
            @SerializedName("primary_image")
            val primaryImage: String?,
            @SerializedName("region")
            val region: String?,
            @SerializedName("title")
            val title: String?
        ) {
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
                @SerializedName("mail")
                val mail: String?,
                @SerializedName("mobile")
                val mobile: Any?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("phone")
                val phone: String?,
                @SerializedName("properties_count")
                val propertiesCount: Int?
            )

            fun toProjectModel(): ProjectHomeModel {
                return ProjectHomeModel(
                    id ?: -1, primaryImage ?: "", if (isFav == "1") TRUE else FALSE,
                    title ?: "", description ?: "", "$region $city", createdAt ?: "", brokerDetails?.get(0)?.logo ?: ""

                )
            }
        }

        data class FeaturedPropertiesCountry(
            @SerializedName("address")
            val address: String?,
            @SerializedName("bath_room_no")
            val bathRoomNo: Int?,
            @SerializedName("bed_rooms_no")
            val bedRoomsNo: Int?,
            @SerializedName("broker_details")
            val brokerDetails: List<BrokerDetail?>?,
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
            @SerializedName("for_what")
            val forWhat: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_fav")
            val isFav: String?,
            @SerializedName("land_area")
            val landArea: Int?,
            @SerializedName("normal_featured")
            val normalFeatured: String?,
            @SerializedName("primary_image")
            val primaryImage: String?,
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
            val title: String?
        ) {
            data class BrokerDetail(
                @SerializedName("broker_type")
                val brokerType: String?,
                @SerializedName("description")
                val description: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("first_name")
                val firstName: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("last_name")
                val lastName: String?,
                @SerializedName("logo")
                val logo: String?,
                @SerializedName("mobile")
                val mobile: Any?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("offers")
                val offers: List<Offer?>?,
                @SerializedName("phone")
                val phone: String?,
                @SerializedName("properties_count")
                val propertiesCount: Int?,
                @SerializedName("src")
                val src: String?,
                @SerializedName("submitted_props")
                val submittedProps: List<SubmittedProp?>?
            ) {
                data class Offer(
                    @SerializedName("created_at")
                    val createdAt: String?,
                    @SerializedName("email")
                    val email: String?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("offer")
                    val offer: String?,
                    @SerializedName("offer_type")
                    val offerType: String?,
                    @SerializedName("phone")
                    val phone: String?,
                    @SerializedName("property")
                    val `property`: Property?,
                    @SerializedName("property_id")
                    val propertyId: Int?,
                    @SerializedName("seen")
                    val seen: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?
                ) {
                    data class Property(
                        @SerializedName("acceptance")
                        val acceptance: String?,
                        @SerializedName("added_by")
                        val addedBy: Int?,
                        @SerializedName("adding_type")
                        val addingType: String?,
                        @SerializedName("address")
                        val address: Address?,
                        @SerializedName("apartment_num")
                        val apartmentNum: Int?,
                        @SerializedName("bath_room_no")
                        val bathRoomNo: Int?,
                        @SerializedName("bed_rooms_no")
                        val bedRoomsNo: Int?,
                        @SerializedName("building_num")
                        val buildingNum: Int?,
                        @SerializedName("category")
                        val category: Int?,
                        @SerializedName("city")
                        val city: Int?,
                        @SerializedName("country")
                        val country: Int?,
                        @SerializedName("created_at")
                        val createdAt: String?,
                        @SerializedName("currency")
                        val currency: Int?,
                        @SerializedName("deleted_at")
                        val deletedAt: Any?,
                        @SerializedName("description")
                        val description: Description?,
                        @SerializedName("finishing")
                        val finishing: Int?,
                        @SerializedName("floor_num")
                        val floorNum: Int?,
                        @SerializedName("for_what")
                        val forWhat: String?,
                        @SerializedName("garage_no")
                        val garageNo: Int?,
                        @SerializedName("garage_size")
                        val garageSize: Int?,
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
                        @SerializedName("on_site")
                        val onSite: String?,
                        @SerializedName("primary_image")
                        val primaryImage: String?,
                        @SerializedName("priority")
                        val priority: String?,
                        @SerializedName("property_type")
                        val propertyType: Int?,
                        @SerializedName("purpose")
                        val purpose: Int?,
                        @SerializedName("reception_floor_type")
                        val receptionFloorType: Int?,
                        @SerializedName("reception_pieces")
                        val receptionPieces: Int?,
                        @SerializedName("region")
                        val region: Int?,
                        @SerializedName("rent_duration")
                        val rentDuration: String?,
                        @SerializedName("rent_price")
                        val rentPrice: Int?,
                        @SerializedName("room_ensuite")
                        val roomEnsuite: Int?,
                        @SerializedName("sale_price")
                        val salePrice: Int?,
                        @SerializedName("title")
                        val title: Title?,
                        @SerializedName("total_area")
                        val totalArea: Int?,
                        @SerializedName("unit_floor")
                        val unitFloor: String?,
                        @SerializedName("updated_at")
                        val updatedAt: String?,
                        @SerializedName("video")
                        val video: String?,
                        @SerializedName("video_type")
                        val videoType: String?,
                        @SerializedName("views")
                        val views: Int?
                    ) {
                        data class Address(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )

                        data class Description(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )

                        data class Title(
                            @SerializedName("ar")
                            val ar: String?,
                            @SerializedName("en")
                            val en: String?
                        )
                    }
                }

                data class SubmittedProp(
                    @SerializedName("acceptance")
                    val acceptance: String?,
                    @SerializedName("added_by")
                    val addedBy: Int?,
                    @SerializedName("adding_type")
                    val addingType: String?,
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("apartment_num")
                    val apartmentNum: Int?,
                    @SerializedName("bath_room_no")
                    val bathRoomNo: Int?,
                    @SerializedName("bed_rooms_no")
                    val bedRoomsNo: Int?,
                    @SerializedName("building_num")
                    val buildingNum: Int?,
                    @SerializedName("category")
                    val category: Int?,
                    @SerializedName("city")
                    val city: Int?,
                    @SerializedName("country")
                    val country: Int?,
                    @SerializedName("created_at")
                    val createdAt: String?,
                    @SerializedName("currency")
                    val currency: Int?,
                    @SerializedName("deleted_at")
                    val deletedAt: Any?,
                    @SerializedName("description")
                    val description: Description?,
                    @SerializedName("finishing")
                    val finishing: Int?,
                    @SerializedName("floor_num")
                    val floorNum: Int?,
                    @SerializedName("for_what")
                    val forWhat: String?,
                    @SerializedName("garage_no")
                    val garageNo: Int?,
                    @SerializedName("garage_size")
                    val garageSize: Int?,
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
                    @SerializedName("on_site")
                    val onSite: String?,
                    @SerializedName("primary_image")
                    val primaryImage: String?,
                    @SerializedName("priority")
                    val priority: String?,
                    @SerializedName("property_type")
                    val propertyType: Int?,
                    @SerializedName("purpose")
                    val purpose: Int?,
                    @SerializedName("reception_floor_type")
                    val receptionFloorType: Int?,
                    @SerializedName("reception_pieces")
                    val receptionPieces: Int?,
                    @SerializedName("region")
                    val region: Int?,
                    @SerializedName("rent_duration")
                    val rentDuration: String?,
                    @SerializedName("rent_price")
                    val rentPrice: Int?,
                    @SerializedName("room_ensuite")
                    val roomEnsuite: Int?,
                    @SerializedName("sale_price")
                    val salePrice: Int?,
                    @SerializedName("title")
                    val title: Title?,
                    @SerializedName("total_area")
                    val totalArea: Int?,
                    @SerializedName("unit_floor")
                    val unitFloor: String?,
                    @SerializedName("updated_at")
                    val updatedAt: String?,
                    @SerializedName("video")
                    val video: String?,
                    @SerializedName("video_type")
                    val videoType: String?,
                    @SerializedName("views")
                    val views: Int?
                ) {
                    data class Address(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )

                    data class Description(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )

                    data class Title(
                        @SerializedName("ar")
                        val ar: String?,
                        @SerializedName("en")
                        val en: String?
                    )
                }
            }

            fun toPropertyModel(): PropertyModel {
                return PropertyModel(
                    id ?: -1,
                    primaryImage ?: "",
                    forWhat ?: "",
                    if (isFav == "1") TRUE else FALSE,
                    if (normalFeatured == AdvertisementType.FEATURED.key) TRUE else FALSE,
                    salePrice?.toDouble() ?: 0.0,
                    rentPrice?.toDouble() ?: 0.0,
                    rentDuration ?: "",
                    title ?: "",
                    landArea ?: 0,
                    bathRoomNo ?: 0,
                    bedRoomsNo ?: 0,
                    "$region, $city",
                    createdAt ?: "",
                    brokerDetails?.get(0)?.logo ?: ""
                )
            }
        }

        data class New(
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("summary")
            val summary: String?,
            @SerializedName("title")
            val title: String?
        )

        data class PropertyInCity(
            @SerializedName("for_rent")
            val forRent: Int?,
            @SerializedName("for_sell")
            val forSell: Int?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("title")
            val title: String?
        ) {
            fun toPropertiesByCityModel(): PropertiesByCityModel {
                return PropertiesByCityModel(id ?: -1, image ?: "", title ?: "", forSell ?: -1, forRent ?: -1)
            }
        }

        data class Slider(
            @SerializedName("description")
            val description: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("subtitle")
            val subtitle: String?,
            @SerializedName("title")
            val title: String?
        )

        data class Villa(
            @SerializedName("address")
            val address: String?,
            @SerializedName("bath_room_no")
            val bathRoomNo: Int?,
            @SerializedName("bed_rooms_no")
            val bedRoomsNo: Int?,
            @SerializedName("broker_details")
            val brokerDetails: List<BrokerDetail?>?,
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
            @SerializedName("for_what")
            val forWhat: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_fav")
            val isFav: String?,
            @SerializedName("land_area")
            val landArea: Int?,
            @SerializedName("normal_featured")
            val normalFeatured: String?,
            @SerializedName("primary_image")
            val primaryImage: String?,
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
            val title: String?
        ) {
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
        }
    }

}