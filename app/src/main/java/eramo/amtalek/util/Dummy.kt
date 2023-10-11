package eramo.amtalek.util

import android.content.Context
import eramo.amtalek.R
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.dummy.AlbumModel
import eramo.amtalek.domain.model.dummy.CountriesSpinnerModel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

object Dummy {

    fun list(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..3) list.add(i.toString())
        return list
    }

    fun carouselList(): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
//        list.add(
//            CarouselItem(
//                imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080",
//                caption = "Photo by Aaron Wu on Unsplash"
//            )
//        )
        list.add(CarouselItem(imageDrawable = R.color.amtalek_blue))
        list.add(CarouselItem(imageDrawable = R.color.events_pink))
        return list
    }

    fun dummyAlbumList(): ArrayList<AlbumModel> {
        val list = ArrayList<AlbumModel>()
        list.add(AlbumModel(imgUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080"))
        list.add(AlbumModel(imgUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"))
        list.add(AlbumModel(imgUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"))
        list.add(AlbumModel(resId = R.drawable.pic_avatar))
        list.add(AlbumModel(resId = R.drawable.pic_avatar))
        return list
    }

    fun dummyProperties(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Gas temperature")
        list.add("Balcony")
        list.add("Conditioning")
        list.add("Swimming pool")
        list.add("Washer and dryer")
        list.add("The fireplace")
        list.add("Smoking allowed")
        list.add("Home theatre")
        list.add("Washroom")
        list.add("Window coverings")
        list.add("Cable TV")
        return list
    }

    fun dummyBoard(): ArrayList<OnBoardingModel> {
        val list = ArrayList<OnBoardingModel>()
        list.add(
            OnBoardingModel(
                splashTitle = "",
                splashDetails = "We offer you the best and most comfortable properties To live a better life for you and your family",
                imgRes = R.drawable.pic_onboarding_one
            )
        )
        list.add(
            OnBoardingModel(
                splashTitle = "",
                splashDetails = "Move around and choose among many distinctive properties that suit your needs at the best price",
                imgRes = R.drawable.pic_onboarding_two
            )
        )
        list.add(
            OnBoardingModel(
                splashTitle = "",
                splashDetails = "We have provided you with the possibility of investing in your property, selling or renting and find the best return for you",
                imgRes = R.drawable.pic_onboarding_three
            )
        )
//        list.add(
//            OnBoardingModel(
//                splashTitle = "",
//                splashDetails = "We have provided you with the possibility of investing in your property, selling or renting and find the best return for you",
//                imgRes = R.drawable.pic_onboarding_three
//            )
//        )
        return list
    }

    fun dummyCarouselList(): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080",
                caption = "Photo by Aaron Wu on Unsplash"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080",
                headers = headers
            )
        )
        return list
    }

    fun dummyCountriesList():List<CountriesSpinnerModel>{
        val list = mutableListOf<CountriesSpinnerModel>()

        list.add(CountriesSpinnerModel("Country","https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/255px-Flag_of_Egypt.svg.png"))
        list.add(CountriesSpinnerModel("Egypt","https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/255px-Flag_of_Egypt.svg.png"))
        list.add(CountriesSpinnerModel("KSA","https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"))

        return list
    }

    fun dummyCitiesList():List<CountriesSpinnerModel>{
        val list = mutableListOf<CountriesSpinnerModel>()

        list.add(CountriesSpinnerModel("City","https://gcdnb.pbrd.co/images/M5I41IAETit8.png"))
        list.add(CountriesSpinnerModel("Cairo","https://gcdnb.pbrd.co/images/M5I41IAETit8.png"))
        list.add(CountriesSpinnerModel("Alex","https://gcdnb.pbrd.co/images/M5I41IAETit8.png"))

        return list
    }

}