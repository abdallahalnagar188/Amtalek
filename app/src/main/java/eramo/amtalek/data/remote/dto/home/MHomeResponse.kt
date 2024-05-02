package eramo.amtalek.data.remote.dto.home


import com.google.gson.annotations.SerializedName

data class MHomeResponse(
    @SerializedName("data")
    var `data`: List<Data>?)
data class Data(
    @SerializedName("cart_type")
    var cartType: String?,
    @SerializedName("content")
    var content: List<Content>?,
    @SerializedName("section_type")
    var sectionType: String?,
    @SerializedName("title")
    var title: String?
)
data class Content(
    @SerializedName("acceptance")
    var acceptance: String?,
    @SerializedName("added_by")
    var addedBy: String?,
    @SerializedName("address")
    var address: String?,
    @SerializedName("agent_data")
    var agentData: List<AgentData>?,
    @SerializedName("agent_info")
    var agentInfo: List<AgentInfo>?,
    @SerializedName("amenities")
    var amenities: List<Amenity>?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("bedrooms")
    var bedrooms: String?,
    @SerializedName("broker_details")
    var brokerDetails: List<BrokerDetail>?,
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("count")
    var count: Int?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("delivery_date")
    var deliveryDate: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facebook")
    var facebook: String?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("google_plus")
    var googlePlus: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("img")
    var img: String?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("love")
    var love: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("news_category")
    var newsCategory: NewsCategory?,
    @SerializedName("normal_featured")
    var normalFeatured: String?,
    @SerializedName("price_from")
    var priceFrom: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("properties_count")
    var propertiesCount: Int?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("purpose")
    var purpose: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("rent_properties")
    var rentProperties: Int?,
    @SerializedName("role")
    var role: String?,
    @SerializedName("room_ensuite")
    var roomEnsuite: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sales_properties")
    var salesProperties: Int?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("total_units")
    var totalUnits: String?,
    @SerializedName("twitter")
    var twitter: String?,
    @SerializedName("views")
    var views: Int?
)
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
    @SerializedName("projects")
    var projects: List<Project>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
)
data class AgentDataX(
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
)
data class AgentInfo(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("email")
    var email: Any?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("logo")
    var logo: Any?,
    @SerializedName("name")
    var name: Any?,
    @SerializedName("phone")
    var phone: Any?,
    @SerializedName("projects")
    var projects: List<Any?>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
)
data class Amenity(
    @SerializedName("amenities")
    var amenities: Int?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("property_id")
    var propertyId: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?
)
data class NewsCategory(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_title")
    var mainTitle: String?,
    @SerializedName("title")
    var title: Title?
)
data class Project(
    @SerializedName("agent_data")
    var agentData: AgentDataX?,
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
)
data class Title(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
)
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
    var projects: List<Project>?,
    @SerializedName("projects_count")
    var projectsCount: Int?,
    @SerializedName("properties_count")
    var propertiesCount: Int?
)