package eramo.amtalek.domain.model.property

import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.social.RatingCommentsModel

data class PropertyDetailsModel(
    val sliderImages:List<String>,
    val sellPrice:Double,
    val rentPrice:Double,
    val currency:String,
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
    val brokerId:Int,
    val propertyCode:String,
    val propertyType:String,
    val furniture:String,
    val forWhat:String,
    val payment:String,
    val floors:List<Int>,
    val landType:String,
    val description:String,
    val propertyAmenities:List<AmenityModel>,
    val videoUrl:String?,
    val comments:List<RatingCommentsModel>,
    val similarProperties:List<eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel>,
    val chartList:List<ChartModel>,
    val mapUrl:String   ,
    val id:Int,
    val isFavourite:String,
    val sold:Boolean,
    val calcRoi:String,
    val roi:String
    )
