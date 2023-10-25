package eramo.amtalek.domain.model.main.market


data class MarketPostsModel(

    val postType: MarketPostType,

    // ADVERTISEMENT
    val userImageUrl: String,
    val userName: String,
    val datePosted: String,
    val postImageUrl: String?= null,
    val isFavourite: Int?=null,
    val price: Double?= null,
    val postTitle: String?= null,
    val area: Int?= null,
    val bathroomsCount: Int?= null,
    val bedsCount: Int?= null,
    val location: String?= null,

    // TEXT
    val postBody:String? = null,

    // PHOTOS
    val photosList:List<String>? = null,

    val commentsCount: Int,
    val likesCount: Int
)
