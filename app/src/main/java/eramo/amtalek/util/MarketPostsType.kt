package eramo.amtalek.util

enum class MarketPostType{
    ADVERTISEMENT,
    TEXT,
    PHOTOS,
    TEXT_AND_PHOTOS
}
//
//sealed class MarketPostsTypeModel(){
//    data class Text(val ss:String)
//    data class Photos(val ss:String)
//}

data class MarketPostsTypeModel(
    val postType: MarketPostType
)