package eramo.amtalek.domain.model.main.market


data class MarketPostsModel(

    val postType: MarketPostType,

    // ADVERTISEMENT
    val userImageUrl: String,
    val userName: String,
    val datePosted: String,
    val postImageUrl: String,
    val isFavourite: Int,
    val price: Double,
    val postTitle: String,
    val area: Int,
    val bathroomsCount: Int,
    val bedsCount: Int,
    val location: String,

    val commentsCount: Int,
    val likesCount: Int,
)
