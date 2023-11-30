package eramo.amtalek.domain.model.property

import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.domain.model.social.RatingCommentsModel

data class PropertyDetailsModel(
    val sliderImages:List<String>,

    val sellPrice:Double,
    val rentPrice:Double,
    val rentDuration: String,
    val title:String,
    val location:String,
    val datePosted:String,

    val finishing:String,
    val finishingAvailability:String,
    val areaLocation:String,
    val area:Int,
    val bathroomsCount:Int,
    val bedroomsCount:Int,

    val brokerImageUrl:String,
    val brokerName:String,
    val brokerDescription:String,

    val propertyCode:String,
    val propertyType:String,
    val furniture:String,
    val payment:String,
    val floors:List<Int>,
    val landType:String,

    val description:String,
    val propertyFeatures:List<String>,

    val videoUrl:String,
    val comments:List<RatingCommentsModel>,

    val similarProperties:List<PropertyModel>,
)
