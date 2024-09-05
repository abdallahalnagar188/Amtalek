package eramo.amtalek.data.remote.dto.myHome.news


import com.google.gson.annotations.SerializedName

data class NewsDetailsResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) {
    data class Data(
        @SerializedName("added_by")
        var addedBy: String?,
        @SerializedName("agent_info")
        var agentInfo: List<AgentInfo?>?,
        @SerializedName("category_details")
        var categoryDetails: CategoryDetails?,
        @SerializedName("created_at")
        var createdAt: String?,
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
        @SerializedName("latest_news")
        var latestNews: List<LatestNew?>?,
        @SerializedName("summary")
        var summary: String?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("twitter")
        var twitter: String?,
        @SerializedName("views")
        var views: Int?
    ) {
        data class AgentInfo(
            @SerializedName("broker_type")
            var brokerType: String?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("email")
            var email: Any?,
            @SerializedName("has_package")
            var hasPackage: String?,
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
            @SerializedName("property_for_rent")
            var propertyForRent: Int?,
            @SerializedName("property_for_sale")
            var propertyForSale: Int?
        )

        data class CategoryDetails(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("name")
            var name: String?
        )

        data class LatestNew(
            @SerializedName("added_by")
            var addedBy: String?,
            @SerializedName("agent_info")
            var agentInfo: List<AgentInfo?>?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("love")
            var love: Int?,
            @SerializedName("news_category")
            var newsCategory: NewsCategory?,
            @SerializedName("summary")
            var summary: String?,
            @SerializedName("title")
            var title: String?,
            @SerializedName("views")
            var views: Int?
        ) {
            data class AgentInfo(
                @SerializedName("broker_type")
                var brokerType: String?,
                @SerializedName("description")
                var description: String?,
                @SerializedName("email")
                var email: Any?,
                @SerializedName("has_package")
                var hasPackage: String?,
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
                @SerializedName("property_for_rent")
                var propertyForRent: Int?,
                @SerializedName("property_for_sale")
                var propertyForSale: Int?
            )

            data class NewsCategory(
                @SerializedName("id")
                var id: Int?,
                @SerializedName("main_title")
                var mainTitle: String?,
                @SerializedName("title")
                var title: Title?
            ) {
                data class Title(
                    @SerializedName("ar")
                    var ar: String?,
                    @SerializedName("en")
                    var en: String?
                )
            }
        }
    }
}