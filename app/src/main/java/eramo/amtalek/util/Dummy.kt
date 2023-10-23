package eramo.amtalek.util

import android.content.Context
import eramo.amtalek.R
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.drawer.latestprojects.LatestProjectsModel
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.dummy.AlbumModel
import eramo.amtalek.domain.model.dummy.CountriesSpinnerModel
import eramo.amtalek.domain.model.extentions.NotificationsModel
import eramo.amtalek.domain.model.main.brokers.BrokerModel
import eramo.amtalek.domain.model.main.home.NewsModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
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

    fun dummyBoard(context: Context): ArrayList<OnBoardingModel> {
        val list = ArrayList<OnBoardingModel>()
        list.add(
            OnBoardingModel(
                splashTitle = "",
                splashDetails = context.getString(R.string.fake_onboarding_one),
                imgRes = R.drawable.pic_onboarding_one
            )
        )
        list.add(
            OnBoardingModel(
                splashTitle = "",
                splashDetails = context.getString(R.string.fake_onboarding_two),
                imgRes = R.drawable.pic_onboarding_two
            )
        )
        list.add(
            OnBoardingModel(
                splashTitle = "",
                splashDetails = context.getString(R.string.fake_onboarding_three),
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

    fun dummyCountriesList(): List<CountriesSpinnerModel> {
        val list = mutableListOf<CountriesSpinnerModel>()

        list.add(
            CountriesSpinnerModel(
                "Country",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/255px-Flag_of_Egypt.svg.png"
            )
        )
        list.add(
            CountriesSpinnerModel(
                "Egypt",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/255px-Flag_of_Egypt.svg.png"
            )
        )
        list.add(CountriesSpinnerModel("KSA", "https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"))

        return list
    }

    fun dummyCitiesList(): List<CountriesSpinnerModel> {
        val list = mutableListOf<CountriesSpinnerModel>()

        list.add(CountriesSpinnerModel("City", "https://gcdnb.pbrd.co/images/M5I41IAETit8.png"))
        list.add(CountriesSpinnerModel("Cairo", "https://gcdnb.pbrd.co/images/M5I41IAETit8.png"))
        list.add(CountriesSpinnerModel("Alex", "https://gcdnb.pbrd.co/images/M5I41IAETit8.png"))

        return list
    }

    fun dummyMyFavouritesList(context: Context): List<MyFavouritesModel> {
        val list = mutableListOf<MyFavouritesModel>()

        list.add(
            MyFavouritesModel(
                "https://sbnri.com/blog/wp-content/uploads/2021/07/NRI-Property-In-India.jpg",
                context.getString(R.string.for_sell), 0, 0, 500000.0,
                context.getString(R.string.fake_title), 120, 2, 4,
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"

            )
        )

        list.add(
            MyFavouritesModel(
                "https://sbnri.com/blog/wp-content/uploads/2021/07/NRI-Property-In-India.jpg",
                context.getString(R.string.for_sell), 1, 1, 2500000.0,
                context.getString(R.string.fake_title), 1050, 2, 4,
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"

            )
        )
        list.add(
            MyFavouritesModel(
                "https://sbnri.com/blog/wp-content/uploads/2021/07/NRI-Property-In-India.jpg",
                context.getString(R.string.for_sell), 0, 0, 500000.0,
                context.getString(R.string.fake_title), 120, 2, 4,
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"

            )
        )

        list.add(
            MyFavouritesModel(
                "https://sbnri.com/blog/wp-content/uploads/2021/07/NRI-Property-In-India.jpg",
                context.getString(R.string.for_sell), 1, 1, 2500000.0,
                context.getString(R.string.fake_title), 1050, 2, 4,
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"

            )
        )
        list.add(
            MyFavouritesModel(
                "https://sbnri.com/blog/wp-content/uploads/2021/07/NRI-Property-In-India.jpg",
                context.getString(R.string.for_sell), 0, 0, 500000.0,
                context.getString(R.string.fake_title), 120, 2, 4,
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"

            )
        )

        list.add(
            MyFavouritesModel(
                "https://sbnri.com/blog/wp-content/uploads/2021/07/NRI-Property-In-India.jpg",
                context.getString(R.string.for_sell), 1, 1, 2500000.0,
                context.getString(R.string.fake_title), 1050, 2, 4,
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"

            )
        )


        return list
    }

    fun dummyNotificationsList(): List<NotificationsModel> {
        val list = mutableListOf<NotificationsModel>()

        list.add(
            NotificationsModel(
                "https://amtalek.com/amtalekadmin/public/assets/images/logo_en-removebg-preview.png",
                "Amtalek", "Lörem ipsum åke Mattsson innovatör en innovationssystem.", "3 Days ago"

            )
        )


        list.add(
            NotificationsModel(
                "https://imgv3.fotor.com/images/slider-image/a-man-holding-a-camera-with-image-filter.jpg",
                "Random", "Want to get more involved Want to get more involved Want to get more involved", "3 Days ago"

            )
        )

        list.add(
            NotificationsModel(
                "https://amtalek.com/amtalekadmin/public/assets/images/logo_en-removebg-preview.png",
                "Amtalek", "Lörem ipsum åke Mattsson innovatör en innovationssystem.", "3 Days ago"

            )
        )


        list.add(
            NotificationsModel(
                "https://imgv3.fotor.com/images/slider-image/a-man-holding-a-camera-with-image-filter.jpg",
                "Random", "Want to get more involved Want to get more involved Want to get more involved", "3 Days ago"

            )
        )


        return list
    }

    fun dummyLatestProjectsList(): List<LatestProjectsModel> {
        val list = mutableListOf<LatestProjectsModel>()

        list.add(
            LatestProjectsModel(
                "https://imgnew.outlookindia.com/public/uploads/articles/2022/1/14/aranya11.png",
                "Sheikh Zayed Compound", "Sheikh Zayed Giza", "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"
            )
        )

        list.add(
            LatestProjectsModel(
                "https://housing-images.n7net.in/4f2250e8/cb8632c10a1fd97e6aa3e692731579b0/v0/medium/nyati_era-lohegaon-pune-nyati_group.jpeg",
                "Sheikh Zayed Compound", "Sheikh Zayed Giza", "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"
            )
        )

        list.add(
            LatestProjectsModel(
                "https://imgnew.outlookindia.com/public/uploads/articles/2022/1/14/aranya11.png",
                "Sheikh Zayed Compound", "Sheikh Zayed Giza", "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"
            )
        )

        list.add(
            LatestProjectsModel(
                "https://housing-images.n7net.in/4f2250e8/cb8632c10a1fd97e6aa3e692731579b0/v0/medium/nyati_era-lohegaon-pune-nyati_group.jpeg",
                "Sheikh Zayed Compound", "Sheikh Zayed Giza", "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"
            )
        )

        return list
    }

    fun dummyBrokersList(): List<BrokerModel> {
        val list = mutableListOf<BrokerModel>()

        list.add(
            BrokerModel(
                "https://www.era-egypt.com/wp-content/uploads/2021/06/ERA-2004.png",
                "ERA Estate Development Company",
                "A leading real estate company in real estate design and implementation for more than 100 years",
                4, 250
            )
        )

        list.add(
            BrokerModel(
                "https://images.wuzzuf-data.net/files/company_logo/Real-Estate-Broker-in-Maadi--Cairo--Egypt--Egypt-26682-1503846837.jpg",
                "Lana Real Estate Development Company",
                "A leading real estate company in real estate design and implementation for more than 100 years",
                1, 50
            )
        )

        list.add(
            BrokerModel(
                "https://realestatewithshahid.com/wp-content/uploads/2021/10/RES-logo.png",
                "RES Real Estate Development Company",
                "A leading real estate company in real estate design and implementation for more than 100 years",
                3, 200
            )
        )

        return list
    }

    fun dummyPropertiesByCityList(): List<PropertiesByCityModel> {
        val list = mutableListOf<PropertiesByCityModel>()

        list.add(
            PropertiesByCityModel(
                "https://imagevars.gulfnews.com/2020/12/12/egypt_17656d254e6_large.jpg", "Cairo", 210, 150
            )
        )

        list.add(
            PropertiesByCityModel(
                "https://mlrhpz8jmuut.i.optimole.com/cb:Ie5o.50122/w:auto/h:auto/q:mauto/ig:avif/f:best/id:4b9b0c229e43b68b64e45f9e1b43b329/https://www.egypttoursplus.com/is-it-safe-to-travel-to-alexandria-egypt.jpg",
                "Alexandria",
                210,
                150
            )
        )

        list.add(
            PropertiesByCityModel(
                "https://imagevars.gulfnews.com/2020/12/12/egypt_17656d254e6_large.jpg", "Cairo", 210, 150
            )
        )

        list.add(
            PropertiesByCityModel(
                "https://mlrhpz8jmuut.i.optimole.com/cb:Ie5o.50122/w:auto/h:auto/q:mauto/ig:avif/f:best/id:4b9b0c229e43b68b64e45f9e1b43b329/https://www.egypttoursplus.com/is-it-safe-to-travel-to-alexandria-egypt.jpg",
                "Alexandria",
                210,
                150
            )
        )


        return list
    }

    fun dummyNewsList(): List<NewsModel> {
        val list = mutableListOf<NewsModel>()

        list.add(
            NewsModel(
                "https://st.depositphotos.com/1010613/2974/i/450/depositphotos_29748299-stock-photo-young-man-reading-newspaper.jpg",
                "Housing Market Predictions",
                "Changes to the federal funds rate can indirectly influence mortgage rates. Yet, housing market experts are less concerned about one more interest rate hike this year than what the Fed has in store for rates in the coming years."
            )
        )

        list.add(
            NewsModel(
                "https://st4.depositphotos.com/1000128/22519/i/450/depositphotos_225191638-stock-photo-render-illustration-macro-view-printing.jpg",
                "When Will the Housing Market Recover",
                "But Gumbinger says don’t hope they cool too quickly. Rapidly falling rates could create a surge of demand that wipes away any inventory gains, causing home prices to rebound.\n" +
                        "\n"
            )
        )

        list.add(
            NewsModel(
                "https://media-cldnry.s-nbcnews.com/image/upload/t_focal-758x379,f_auto,q_auto:best/rockcms/2022-10/221020-home-sales-al-1433-481d18.jpg",
                "Housing Inventory Outlook",
                "Housing stock remains at near historic lows—especially entry-level supply—consequently propping up demand and sustaining ultra-high home prices."
            )
        )

        return list
    }

}