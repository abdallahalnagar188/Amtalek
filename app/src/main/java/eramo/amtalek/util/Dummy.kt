package eramo.amtalek.util

import android.content.Context
import eramo.amtalek.R
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.drawer.MessagingChatModel
import eramo.amtalek.domain.model.drawer.MessagingOffersModel
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.domain.model.drawer.latestprojects.LatestProjectsModel
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.dummy.AlbumModel
import eramo.amtalek.domain.model.dummy.CountriesSpinnerModel
import eramo.amtalek.domain.model.extentions.NotificationsModel
import eramo.amtalek.domain.model.main.ProjectModel
import eramo.amtalek.domain.model.main.brokers.BrokerModel
import eramo.amtalek.domain.model.main.home.NewsModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.domain.model.main.market.MarketPostType
import eramo.amtalek.domain.model.main.market.MarketPostsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.domain.model.social.messaging.ChatMessageModel
import eramo.amtalek.domain.model.social.messaging.ChatMessageType
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
                context.getString(R.string.for_rent), 1, 1, 2500000.0,
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

    fun dummyProjectsList(context: Context): List<ProjectModel> {
        val list = mutableListOf<ProjectModel>()

        list.add(
            ProjectModel(
                "https://s3.eu-central-1.amazonaws.com/prod.images.cooingestate.com/admin/property_image/image/19665/townhouse.jpg",
                context.getString(R.string.fake_Compound),
                context.getString(R.string.fake_title),
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                1,
                "By SOROUH",
                "https://dreamhomeseg.com/uploads/developers/1667022205.jpg"
            )
        )

        list.add(
            ProjectModel(
                "https://img.sakneen.com/1632740971867-SAltZ.PNG",
                context.getString(R.string.fake_Compound),
                context.getString(R.string.fake_title),
                context.getString(R.string.fake_location),
                context.getString(R.string.fake_date),
                0,
                "By Tallat",
                "https://cairo.realestate/uploads/images/2023-02/logo6.jpg"
            )
        )
        return list
    }

    fun dummyMessagingChatList(): List<MessagingChatModel> {
        val list = mutableListOf<MessagingChatModel>()

        list.add(
            MessagingChatModel(
                "https://preview.keenthemes.com/metronic-v4/theme/assets/pages/media/profile/profile_user.jpg",
                1,
                "Erlan Sadewa",
                "Aight, noted",
                "1h ago",
                2
            )
        )

        list.add(
            MessagingChatModel(
                "https://www.lightstalking.com/wp-content/uploads/Photo-by-Christian-Ferrer-1024x683.jpg",
                1,
                "Athalia Putri",
                "Good morning, did you sleep well?",
                "3m ago",
                12
            )
        )

        list.add(
            MessagingChatModel(
                "https://i.pinimg.com/474x/68/ec/58/68ec58955b672bd6163d3adeb9af4059.jpg",
                1,
                "Evata Blroksil Danial Macolle jack",
                "How is it going? Do you fell good now days",
                "1h ago",
                7
            )
        )

        list.add(
            MessagingChatModel(
                "https://preview.keenthemes.com/metronic-v4/theme/assets/pages/media/profile/profile_user.jpg",
                1,
                "Erlan Sadewa",
                "Aight, noted",
                "1h ago",
                2
            )
        )

        list.add(
            MessagingChatModel(
                "https://www.lightstalking.com/wp-content/uploads/Photo-by-Christian-Ferrer-1024x683.jpg",
                1,
                "Athalia Putri",
                "Good morning, did you sleep well?",
                "3m ago",
                12
            )
        )

        list.add(
            MessagingChatModel(
                "https://i.pinimg.com/474x/68/ec/58/68ec58955b672bd6163d3adeb9af4059.jpg",
                1,
                "Evata Blroksil",
                "How is it going?",
                "1h ago",
                7
            )
        )

        return list
    }

    fun dummyMessagingOffersList(): List<MessagingOffersModel> {
        val list = mutableListOf<MessagingOffersModel>()

        list.add(
            MessagingOffersModel(
                "https://img.freepik.com/free-photo/portrait-smiling-happy-young-man-isolated-white_186202-6708.jpg",
                1,
                "Mike john",
                "Good morning, Got my offer?",
                "2 Days ago",
                2,
                "https://img.freepik.com/free-photo/handsome-young-man-with-arms-crossed-white-background_23-2148222620.jpg",
                "Residential apartment for sale, super",
                350000
            )
        )

        list.add(
            MessagingOffersModel(
                "https://img.freepik.com/free-photo/portrait-smiling-happy-young-man-isolated-white_186202-6708.jpg",
                1,
                "Mike john",
                "Good morning, Got my offer?",
                "2 Days ago",
                2,
                "https://img.freepik.com/free-photo/handsome-young-man-with-arms-crossed-white-background_23-2148222620.jpg",
                "Residential apartment for sale, super",
                350000
            )
        )



        return list
    }

    fun dummyMarketPostsList(): MutableList<MarketPostsModel> {
        val list = mutableListOf<MarketPostsModel>()
        val imagesList = mutableListOf<String>()

        imagesList.add("https://st.depositphotos.com/2249091/2496/i/450/depositphotos_24962203-stock-photo-travertine-house-modern-living-room.jpg")
        imagesList.add("https://previews.123rf.com/images/bialasiewicz/bialasiewicz1501/bialasiewicz150101112/35845723-horizontal-view-of-living-space-inside-house.jpg")
        imagesList.add("https://page-assets.foxtons.co.uk/news-images/2018/01/london-property-transformation-1/720.jpg")
        imagesList.add("https://st.depositphotos.com/2249091/2496/i/450/depositphotos_24962203-stock-photo-travertine-house-modern-living-room.jpg")
        imagesList.add("https://thumbs.dreamstime.com/b/house-interior-hallway-classic-staircase-entrance-contrast-hardwood-floor-carpet-covered-43909337.jpg")
        imagesList.add("https://page-assets.foxtons.co.uk/news-images/2018/01/london-property-transformation-1/720.jpg")
        imagesList.add("https://page-assets.foxtons.co.uk/news-images/2018/01/london-property-transformation-1/720.jpg")

        list.add(
            MarketPostsModel(
                MarketPostType.ADVERTISEMENT,
                userImageUrl = "https://images.ctfassets.net/lh3zuq09vnm2/yBDals8aU8RWtb0xLnPkI/19b391bda8f43e16e64d40b55561e5cd/How_tracking_user_behavior_on_your_website_can_improve_customer_experience.png",
                userName = "Ahmed Ali",
                datePosted = "4 Days ago",
                postImageUrl = "https://www.semsarmasr.com/media/238/realestate_%D8%B4%D9%82%D8%A9-%D8%A8%D8%AD%D8%B1%D9%8A%D8%A9-%D8%B3%D9%88%D8%A8%D8%B1-%D9%84%D9%88%D9%83%D8%B3-130%D9%85%D8%AA%D8%B1-%D9%85%D9%88%D9%82%D8%B9-%D9%85%D8%AA%D9%85%D9%8A%D8%B2-%D9%83%D9%85%D8%A8%D9%88%D9%86%D8%AF-%D8%AF%D8%A7%D8%B1-%D9%85%D8%B5%D8%B1-%D8%A7%D9%84%D8%AD%D9%8A-%D8%A7%D9%84%D9%85%D8%AA%D9%85%D9%8A%D8%B2-%D9%85%D8%AF%D9%8A%D9%86%D8%A9-%D8%A8%D8%AF%D8%B1_120230902242423.jpg",
                isFavourite = 0,
                price = 450000.0,
                postTitle = "Department in badr city with good location",
                area = 130,
                bathroomsCount = 1,
                bedsCount = 4,
                location = "Badr city",
                likesCount = 14,
                commentsCount = 5

            )
        )

        list.add(
            MarketPostsModel(
                MarketPostType.ADVERTISEMENT,
                userImageUrl = "https://img.freepik.com/free-photo/portrait-smiling-happy-young-man-isolated-white_186202-6708.jpg",
                userName = "Yousif Gamal",
                datePosted = "2 Days ago",
                postImageUrl = "https://www.luxorpropertysales.com/wp-content/uploads/2022/08/IMG-20220515-WA0063-488x326.jpg",
                isFavourite = 1,
                price = 25000000.0,
                postTitle = "Department in badr city with good location",
                area = 330,
                bathroomsCount = 4,
                bedsCount = 8,
                location = "Badr city",
                likesCount = 10,
                commentsCount = 19

            )
        )

        list.add(
            MarketPostsModel(
                MarketPostType.TEXT,

                userName = "Amr Tarik",
                userImageUrl = "https://images.ctfassets.net/lh3zuq09vnm2/yBDals8aU8RWtb0xLnPkI/19b391bda8f43e16e64d40b55561e5cd/How_tracking_user_behavior_on_your_website_can_improve_customer_experience.png",
                datePosted = "Now",
                postBody = "Permission is hereby granted, free of charge, to any person obtaining a copy" +
                        "of this software and associated documentation files to deal" +
                        "in the Software without restriction, including without limitation the rights" +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell" +
                        "copies of the Software, and to permit persons to whom the Software is" +
                        "furnished to do so, subject to the following conditions",
                likesCount = 10,
                commentsCount = 19

            )
        )

        list.add(
            MarketPostsModel(
                MarketPostType.PHOTOS,

                userName = "Maged",
                userImageUrl = "https://img.freepik.com/free-photo/portrait-smiling-happy-young-man-isolated-white_186202-6708.jpg",
                datePosted = "Yesterday",
                photosList = imagesList,
                likesCount = 10,
                commentsCount = 19

            )
        )

        list.add(
            MarketPostsModel(
                MarketPostType.TEXT_AND_PHOTOS,

                userName = "Amr Tarik",
                userImageUrl = "https://images.ctfassets.net/lh3zuq09vnm2/yBDals8aU8RWtb0xLnPkI/19b391bda8f43e16e64d40b55561e5cd/How_tracking_user_behavior_on_your_website_can_improve_customer_experience.png",
                datePosted = "Now",
                postBody = "Permission is hereby granted, free of charge, to any person obtaining a copy" +
                        "of this software and associated documentation files to deal" +
                        "in the Software without restriction, including without limitation the rights" +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell" +
                        "copies of the Software, and to permit persons to whom the Software is" +
                        "furnished to do so, subject to the following conditions",
                photosList = mutableListOf("https://previews.123rf.com/images/bialasiewicz/bialasiewicz1211/bialasiewicz121100028/16119427-inside-of-stylish-and-new-flat-living-room.jpg"),
                likesCount = 10,
                commentsCount = 19

            )
        )


        list.add(
            MarketPostsModel(
                MarketPostType.TEXT,

                userName = "Amr Tarik",
                userImageUrl = "https://images.ctfassets.net/lh3zuq09vnm2/yBDals8aU8RWtb0xLnPkI/19b391bda8f43e16e64d40b55561e5cd/How_tracking_user_behavior_on_your_website_can_improve_customer_experience.png",
                datePosted = "Now",
                postBody = "Permission is hereby granted, free of charge, to any person obtaining a copy" +
                        "of this software and associated documentation files to deal" +
                        "in the Software without restriction, including without limitation the rights" +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell" +
                        "copies of the Software, and to permit persons to whom the Software is" +
                        "furnished to do so, subject to the following conditions",
                likesCount = 10,
                commentsCount = 19

            )
        )
        return list
    }

    fun dummyPackagesList(): MutableList<PackageModel> {
        val list = mutableListOf<PackageModel>()

        list.add(
            PackageModel(
                "Free",
                "is free and will always free",
                0,
                5,
                1,
                1,
                10,
                25,
                "#1E617A",
                "#1E617A"
            )
        )

        list.add(
            PackageModel(
                "Basic",
                "FOR SMALL BUSSINESS",
                500,
                15,
                5,
                2,
                30,
                25,
                "#1E617A",
                "#1E617A"
            )
        )

        list.add(
            PackageModel(
                "Pro",
                "FOR MEDIUM BUSSINESS",
                1500,
                150,
                10,
                3,
                150,
                25,
                "#E74335",
                "#EAB010"
            )
        )

        return list
    }

    fun dummyRatingCommentsList(): MutableList<RatingCommentsModel> {
        val list = mutableListOf<RatingCommentsModel>()

        list.add(
            RatingCommentsModel(
                "Abdallah",
                "abdalla98",
                "https://i.pinimg.com/564x/86/62/65/866265369891b45de4a7241df1c633b3.jpg",
                "Lörem ipsum epibäras Gunnar Björklund content manager, content editor, Thomas Berglund prer. Big data kontris.",
                "Tuesday: May 21, 2023",
                4.5f
            )
        )

        list.add(
            RatingCommentsModel(
                "Ahmed Ali",
                "ahmed776",
                "https://www.hairpalace.co.uk/wp-content/uploads/2023/06/Christian-Bale-long-hair.jpg",
                "Lörem ipsum epibäras Gunnar Björklund content manager, content editor, Thomas Berglund prer. Big data kontris. Lörem ipsum epibäras Gunnar Björklund content manager Lörem ipsum epibäras Gunnar Björklund content manager",
                "Tuesday: May 21, 2023",
                3.5f
            )
        )

        list.add(
            RatingCommentsModel(
                "Abdallah",
                "abdalla98",
                "https://i.pinimg.com/564x/86/62/65/866265369891b45de4a7241df1c633b3.jpg",
                "تناولت الحلقة: 1- القدرة تتعلق بالمقدورات (بالأشياء المقدورة) تعلقًا صَلوحيًّا قديمًا وتعلقًا تنجيزيًّا حادثًا 2- الإرادة لها تعلقان: صلوحي قديم وتنجيزي قديم 3- العلم له تعلق تنجيزي قديم فقط 4- الكلام والسمع والبصر لها ثلاثة تعلقات: صلوحي قديم وتنجيزي قديم وتنجيزي حادث 6- التنجيزي الحادث هو ما يعبر عنه الحنفية بالتكوين 7- الإمام الأشعري نقض بناء المعتزلة 8- نهضة الأمة بالرد على أهل الضلال بالعلم 9- لا مشيئة للعباد إلا ما شاء الله لهم."
                        ,
                "Tuesday: May 21, 2023",
                4.5f
            )
        )

        list.add(
            RatingCommentsModel(
                "Ahmed Ali",
                "ahmed776",
                "https://www.hairpalace.co.uk/wp-content/uploads/2023/06/Christian-Bale-long-hair.jpg",
                "Lörem ipsum epibäras Gunnar Björklund content manager, content editor, Thomas Berglund prer. Big data kontris.",
                "Tuesday: May 21, 2023",
                3.5f
            )
        )


        return list
    }

    fun dummyCarouselImagesList(): MutableList<CarouselItem> {
        val list = mutableListOf<CarouselItem>()

        list.add(
            CarouselItem(
                imageUrl = "https://resize.indiatvnews.com/en/resize/newbucket/715_-/2020/07/breakingnews-live-blog-1568185450-1595123397-1596072840.jpg"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/BBC_News_2022_%28Alt%29.svg/1200px-BBC_News_2022_%28Alt%29.svg.png"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://static.vecteezy.com/system/resources/thumbnails/006/299/370/original/world-breaking-news-digital-earth-hud-rotating-globe-rotating-free-video.jpg"
            )
        )


        return list
    }

    fun dummyCarouselPropertiesImagesList(): MutableList<CarouselItem> {
        val list = mutableListOf<CarouselItem>()

        list.add(
            CarouselItem(
                imageUrl = "https://previews.123rf.com/images/bialasiewicz/bialasiewicz1211/bialasiewicz121100028/16119427-inside-of-stylish-and-new-flat-living-room.jpg"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://www.luxorpropertysales.com/wp-content/uploads/2022/08/IMG-20220515-WA0063-488x326.jpg"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://previews.123rf.com/images/bialasiewicz/bialasiewicz1211/bialasiewicz121100028/16119427-inside-of-stylish-and-new-flat-living-room.jpg"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://www.luxorpropertysales.com/wp-content/uploads/2022/08/IMG-20220515-WA0063-488x326.jpg"
            )
        )



        return list
    }

    fun dummyCarouselAutocadImagesList(): MutableList<CarouselItem> {
        val list = mutableListOf<CarouselItem>()

        list.add(
            CarouselItem(
                imageUrl = "https://dwgmodels.com/uploads/posts/2016-10/1476942695_two_story_house_plans.jpg"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/736x/41/ce/9f/41ce9fa7e141ecb159685618c0261ce0.jpg"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/736x/ec/e4/98/ece498700c3cf179090133257eb889ed.jpg"
            )
        )

        return list
    }

    fun dummyChatList(): MutableList<ChatMessageModel> {
        val list = mutableListOf<ChatMessageModel>()

        list.add(
            ChatMessageModel(
                ChatMessageType.SENDER,"How much does the property cost"
            )
        )

        list.add(
            ChatMessageModel(
                ChatMessageType.RECEIVER,"What is your recommendation? What is your recommendation "
            )
        )

        list.add(
            ChatMessageModel(
                ChatMessageType.SENDER,"How much does the property cost How much does the property cost How much does the property cost"
            )
        )

        list.add(
            ChatMessageModel(
                ChatMessageType.RECEIVER,"What is your recommendation?"
            )
        )



        return list
    }
}