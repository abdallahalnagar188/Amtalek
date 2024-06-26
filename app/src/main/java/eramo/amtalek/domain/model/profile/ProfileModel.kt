package eramo.amtalek.domain.model.profile

import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.Favorite
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.MyProp
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.OffersItem

data class ProfileModel (
    val id:Int,
    val firstName:String,
    val lastName:String,
    val email:String,
    val phone:String,
    val image:String,
    val actorType: String,
    val dashboardLink:String,
    val cityName:String,
    val cityId: Int,
    val countryName:String,
    val countryId: Int,
    val isVerified:String,
    val favList:List<Favorite>,
    val myProperties:List<MyProp>,
    val offers:List<OffersItem>,
    val hasPackage:String,
    val bio:String,

    )