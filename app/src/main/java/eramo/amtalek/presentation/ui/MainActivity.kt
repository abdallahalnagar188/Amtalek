package eramo.amtalek.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.ActivityMainBinding
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.dialog.WarningDialog
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.WebViewLocaleHelper
import eramo.amtalek.util.deeplink.DeeplinkHandler
import eramo.amtalek.util.deeplink.DeeplinkHandlerImpl
import eramo.amtalek.util.deeplink.DeeplinkUtil
import eramo.amtalek.util.hideSoftKeyboard
import eramo.amtalek.util.setupLangChooser
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    DeeplinkHandler by DeeplinkHandlerImpl() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModelShared: SharedViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.init(window)
        UserUtil.init(this)
        LoadingDialog.init(this)
        WarningDialog.init(this)
        LocalUtil.init(this)
        LocalUtil.loadLocal(this)
        WebViewLocaleHelper(this).implementWorkaround()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        // Obtain the FirebaseAnalytics instance.
// Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            val token = it.result
            UserUtil.saveFireBaseToken(token)
            Log.e("alo", token)
        }
        //setup navStart
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_navHost) as NavHostFragment
        navController = navHostFragment.findNavController()
        onNewIntent(intent)

        binding.apply {
            mainBn.background = null
            mainBn.menu.getItem(4).setOnMenuItemClickListener() {
                if (UserUtil.isUserLogin()) {
                    navController.navigate(R.id.myProfileFragment)
                } else {
                    navController.navigate(R.id.loginDialog)
                }
                true
            }
            mainBn.setupWithNavController(navController)
            mainFabHome.setOnClickListener {
                navController.popBackStack(R.id.homeFragment, false)
                mainFabHome.setColorFilter(resources.getColor(R.color.red))
            }

            viewModelShared.openDrawer.observe(this@MainActivity) { isOpen ->
                if (isOpen) {
                    hideSoftKeyboard()
                    mainDrawerLayout.openDrawer(GravityCompat.START)
                    viewModelShared.openDrawer.value = false
                }
            }
            if (LocalUtil.isEnglish()) {
                binding.inDrawerHeader.navHeaderIvLogo.setImageDrawable(this@MainActivity.getDrawable(R.drawable.top_logo_en))

            } else {
                binding.inDrawerHeader.navHeaderIvLogo.setImageDrawable(this@MainActivity.getDrawable(R.drawable.top_logo_ar))
            }
//            viewModelShared.profileData.observe(this@MainActivity) { member ->
//                inDrawerHeader.apply {
//                    navHeaderTvUserName.text = member.userName
////                    UserUtil.saveUserProfile(AmtalekApi.IMAGE_URL_PROFILE + member.mImage!!)
//                    Glide.with(this@MainActivity)
//                        .load(AmtalekApi.IMAGE_URL_PROFILE + member.mImage)
//                        .into(navHeaderIvProfile)
//                }
//            }

        }
        fetchProfileDataState()
        fetchLogoutState()
        setupDrawer()
        setupNavBottomVisibility()
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }

    }

    var hasNotificationPermissionGranted = false
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    }
                }
            }
        }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()

    }

    // Hide keyboard onTouch outside
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink(intent, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupDrawer() {
        binding.inDrawerHeader.apply {

            setupLangChooser(
                this@MainActivity,
                layoutLangIvFlag,
                layoutLangCvHeader,
                layoutLangCvBody,
                layoutLangArrow,
                layoutLangIvCheckEn,
                layoutLangIvCheckAr,
                layoutLangLinChoiceEn,
                layoutLangLinChoiceAr
            )

            navHeaderIvBack.setOnClickListener {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

            navHeaderClUserCell.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    navController.navigate(R.id.myProfileFragment)
                } else {
                    navController.navigate(R.id.loginDialog)
                }

                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

            navHeaderMyFavourite.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    navController.navigate(R.id.favouritesFragment)
                } else {
                    navController.navigate(R.id.loginDialog)
                }

                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

            navHeaderNotifications.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    navController.navigate(R.id.notificationFragment)
                } else {
                    navController.navigate(R.id.loginDialog)
                }

                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

//            navHeaderNewProjects.setOnClickListener {
////                navController.navigate(R.id.latestProjectsFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

//            navHeaderMessaging.setOnClickListener {
////                navController.navigate(R.id.messagingFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }
            navHeaderPricing.setOnClickListener() {
                navController.navigate(R.id.packagesFragment)
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

            navHeaderAddYourProperty.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    if (UserUtil.getUserType() == "user") {
                        navController.navigate(R.id.addPropertyFirstFragment)
                    } else {
                        navController.navigate(R.id.myProfileFragment)
                    }
                } else {
                    navController.navigate(R.id.loginDialog)
                }

                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

//            navHeaderBuyPackages.setOnClickListener {
////                navController.navigate(R.id.packagesFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

            navHeaderProfileTab.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    navController.navigate(R.id.myProfileFragment)
                } else {
                    navController.navigate(R.id.loginDialog)
                }
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }

            navHeaderSignOut.setOnClickListener {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                if (UserUtil.isUserLogin()) {
                    viewModelShared.logout()
                } else {
                    navController.navigate(
                        NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                        NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                    )

                }
            }

//            navHeaderTvProjects.setOnClickListener {
//                navController.navigate(R.id.projectsFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

//            navHeaderTvMyPropertiesOffer.setOnClickListener {
//                navController.navigate(R.id.myOffersFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

//            navHeaderTvFavProperties.setOnClickListener {
//                navController.navigate(R.id.favouritesFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

//            navHeaderTvAddProperty.setOnClickListener {
//                navController.navigate(R.id.addPropertyFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

//            navHeaderTvLanguage.setOnClickListener {
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//
//                if (LocalUtil.isEnglish()) {
//                    LocalUtil.setLocal(this@MainActivity, "ar")
//                    ActivityCompat.recreate(this@MainActivity)
//                } else {
//                    LocalUtil.setLocal(this@MainActivity, "en")
//                    ActivityCompat.recreate(this@MainActivity)
//                }
//            }

//            navHeaderTvLogout.setOnClickListener {
//                if (UserUtil.isUserLogin()) {
//                    UserUtil.clearUserInfo()
//                    navController.navigate(
//                        NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
//                        NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
//                    )
//                } else {
//                    navController.navigate(
//                        NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build()
//                    )
//                }
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }
        }
    }

    private fun setupNavBottomVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            when (destination.id) {
                R.id.sliderZoomFragment,
                R.id.suspendDialog,
                R.id.datePickerDialog,
                R.id.deletedAccountDialogFragment,
                R.id.contactUsAuthFragment,
                R.id.informationFragment,
                R.id.termsAndConditionsFragment,
                R.id.forgetPasswordFragment,
                R.id.changePassowrdForgetPasswordFragment,
                R.id.otpForgetPasswordFragment,
                R.id.signUpFragment,
                R.id.otpSignUpFragment,
                R.id.loginFragment,
                R.id.onBoardingFragment,
                R.id.splashFragment,
                R.id.loginDialog,
                R.id.selectCityFragment,
                R.id.changePasswordFragment,
                R.id.editPersonalDetailsFragment,
                R.id.myAccountFragment,
                R.id.myProfileFragment,
                R.id.favouritesFragment,
                R.id.notificationFragment,
                R.id.notificationDetailsFragment,
                R.id.messagingFragment,
                R.id.packagesFragment,
                R.id.myAddPropertyFragmentFragment,
                R.id.latestProjectsFragment,
                R.id.joinUsFragment,
                R.id.completedProjectsFragment,
                R.id.satisfiedCustomersFragment,
                R.id.newsDetailsFragment,
                R.id.offersChatFragment,
                R.id.usersChatFragment,
                R.id.userProfileFragment,
                R.id.seeMorePropertiesFragment,
                R.id.seeMoreProjectsFragment,
                R.id.seeMorePropertiesByCityFragment,
                R.id.propertyDetailsSellFragment,
                R.id.propertyDetailsFragment,
                R.id.propertyDetailsSellAndRentFragment,
                R.id.searchPropertyFragment,
                R.id.searchPropertyResultFragment,
                R.id.sortFragment,
                R.id.myProjectDetailsFragment,
                R.id.packageDetailsFragment,
                R.id.rechargePackageFragment,
                R.id.addCardFragment,
                R.id.buyPackageStepOneFragment,
                R.id.buyPackageStepTwoFragment,
                R.id.imagesListFragment,
                R.id.imageViewFragment,
                R.id.cancelDialog,
                R.id.addPropertyFirstFragment,
                R.id.addPropertySecondFragment,
                R.id.addPropertyThirdFragment,
                R.id.addPropertyFourthFragment,
                R.id.addPropertyFifthFragment,
                R.id.searchFormFragment,
                R.id.searchResultFragment,
                R.id.seeMoreNewsFragment,
                R.id.addAdomsFragment

                -> {
                    binding.apply {
                        mainBottomAppBar.visibility = View.GONE
                        mainFabHome.visibility = View.GONE
                    }
                }

                R.id.brokersDetailsFragment -> {
                    binding.apply {
                        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        mainBottomAppBar.visibility = View.VISIBLE
                        mainFabHome.visibility = View.VISIBLE
                    }
                }

                R.id.homeFragment -> {
                    binding.apply {
                        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        mainBottomAppBar.visibility = View.VISIBLE
                        mainFabHome.visibility = View.VISIBLE
                    }
                }

                else -> {
                    binding.apply {
                        mainBottomAppBar.visibility = View.VISIBLE
                        mainFabHome.visibility = View.VISIBLE
                    }
                }
            }
            when (destination.id) {
                R.id.brokersFragment, R.id.marketFragment, R.id.hotOffersFragment, R.id.myEstateFragment -> {
                    binding.mainFabHome.setColorFilter(resources.getColor(R.color.amtalek_blue))
                }
            }
        }
    }

    private fun setUserInfo() {
        if (UserUtil.isUserLogin()) {
            binding.inDrawerHeader.apply {
                viewModelShared.profileNameState.observe(this@MainActivity) {
                    if (it.isNullOrEmpty()) {
                        if (UserUtil.getUserType() == "user") {
                            navHeaderTvUserName.text =
                                getString(R.string.S_user_name, UserUtil.getUserFirstName(), UserUtil.getUserLastName())
                            navHeaderTvUserCity.text = UserUtil.getCityName()
                        } else if (UserUtil.getUserType() == "broker") {
                            navHeaderTvUserName.text = UserUtil.getBrokerName()
                            navHeaderTvUserCity.text = UserUtil.getUserPhone()
                        }

                    } else {
                        navHeaderTvUserName.text = it
                    }
                }
                viewModelShared.profileCityState.observe(this@MainActivity) {
                    if (it.isNullOrEmpty()) {
                        if (UserUtil.getUserType() == "user") {
                            navHeaderTvUserCity.text = UserUtil.getCityName()
                        } else if (UserUtil.getUserType() == "broker") {
                            navHeaderTvUserCity.text = UserUtil.getUserPhone()
                        }
                    } else {
                        navHeaderTvUserCity.text = it
                    }
                }
                navHeaderTvSignOut.text = getString(R.string.sign_out)
                viewModelShared.profileImageUri.observe(this@MainActivity) {
                    Glide.with(this@MainActivity)
                        .load(it)
                        .into(navHeaderIvProfile)
                }

                Glide.with(this@MainActivity)
                    .load(
                        if (UserUtil.getUserProfileImageUrl() != "") {
                            UserUtil.getUserProfileImageUrl()
                        } else {
                            R.drawable.avatar
                        }

                    )
                    .into(navHeaderIvProfile)
            }
        } else if (!UserUtil.isUserLogin()) {
            binding.inDrawerHeader.apply {
                navHeaderTvUserName.text = getString(R.string.guest_mode)
                navHeaderTvUserCity.text = ""

                navHeaderTvSignOut.text = getString(R.string.sign_in)

                Glide.with(this@MainActivity)
                    .load(R.drawable.avatar)
                    .into(navHeaderIvProfile)
            }
        }
    }

    private fun fetchProfileDataState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelShared.LoginData.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            setUserInfo()
                            binding.inDrawerHeader.apply {
                                if (state.data?.actorType == "broker") {
                                    navHeaderTvUserName.text = state.data.name
                                } else if (state.data?.actorType == "user") {
                                    navHeaderTvUserName.text =
                                        getString(R.string.S_user_name, state.data?.firstName, state.data?.lastName)
                                }
                                Log.e("cityy", state.data?.cityName!!)
                                navHeaderTvUserCity.text = state.data?.cityName
                                Glide.with(this@MainActivity)
                                    .load(
                                        if (state.data?.userImage != "") {
                                            state.data?.userImage
                                        } else {
                                            R.drawable.avatar
                                        }
                                    )
                                    .into(navHeaderIvProfile)
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(this@MainActivity)
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun fetchLogoutState() {
        lifecycleScope.launch {
            viewModelShared.logoutState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        setUserInfo()
                        navController.navigate(
                            NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                            NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                        )

                        LoadingDialog.dismissDialog()
                    }

                    is UiState.Error -> {
                        LoadingDialog.dismissDialog()
                        val errorMessage = state.message!!.asString(this@MainActivity)
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()

                        navController.navigate(
                            NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                            NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                        )
                    }

                    is UiState.Loading -> {
                        LoadingDialog.showDialog()

                    }

                    else -> Unit

                }
            }
        }
    }
}