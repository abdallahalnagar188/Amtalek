package eramo.amtalek.data.remote.dto.userDetials.response


import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("acceptance")
    var acceptance: String?,
    @SerializedName("added_by")
    var addedBy: Int?,
    @SerializedName("adding_type")
    var addingType: String?,
    @SerializedName("address")
    var address: Address?,
    @SerializedName("apartment_num")
    var apartmentNum: Int?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("building_num")
    var buildingNum: Int?,
    @SerializedName("calc_roi")
    var calcRoi: String?,
    @SerializedName("category")
    var category: Int?,
    @SerializedName("city")
    var city: Int?,
    @SerializedName("company_id")
    var companyId: Int?,
    @SerializedName("country")
    var country: Int?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("creator_type")
    var creatorType: String?,
    @SerializedName("currency")
    var currency: Int?,
    @SerializedName("deleted_at")
    var deletedAt: Any?,
    @SerializedName("description")
    var description: Description?,
    @SerializedName("finishing")
    var finishing: Int?,
    @SerializedName("floor_num")
    var floorNum: Int?,
    @SerializedName("for_rent_price_roi")
    var forRentPriceRoi: Int?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("garage_no")
    var garageNo: Int?,
    @SerializedName("garage_size")
    var garageSize: Int?,
    @SerializedName("have_garage")
    var haveGarage: String?,
    @SerializedName("id")
    var id: Int?,
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
    @SerializedName("on_site")
    var onSite: String?,
    @SerializedName("our_commission")
    var ourCommission: String?,
    @SerializedName("owner_id")
    var ownerId: Int?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("property_type")
    var propertyType: Int?,
    @SerializedName("purpose")
    var purpose: Int?,
    @SerializedName("reception_floor_type")
    var receptionFloorType: Int?,
    @SerializedName("reception_pieces")
    var receptionPieces: Int?,
    @SerializedName("region")
    var region: Int?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("residential_commercial")
    var residentialCommercial: String?,
    @SerializedName("roi")
    var roi: Double?,
    @SerializedName("room_ensuite")
    var roomEnsuite: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sale_price_roi")
    var salePriceRoi: Any?,
    @SerializedName("sold")
    var sold: String?,
    @SerializedName("sub_region")
    var subRegion: Int?,
    @SerializedName("title")
    var title: Title?,
    @SerializedName("total_area")
    var totalArea: Int?,
    @SerializedName("unit_floor")
    var unitFloor: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("video")
    var video: String?,
    @SerializedName("video_type")
    var videoType: String?,
    @SerializedName("views")
    var views: Int?
)