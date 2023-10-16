package eramo.amtalek.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.R
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.databinding.ActivityMainBinding
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.dialog.WarningDialog
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.*
import eramo.amtalek.util.Constants.TAG
import eramo.amtalek.util.Constants.TOPIC
import eramo.amtalek.util.deeplink.DeeplinkUtil
import eramo.amtalek.util.deeplink.DeeplinkHandler
import eramo.amtalek.util.deeplink.DeeplinkHandlerImpl
import eramo.amtalek.util.notification.FirebaseMessageReceiver

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    DeeplinkHandler by DeeplinkHandlerImpl() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModelShared: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.init(window)
        UserUtil.init(this)
        LoadingDialog.init(this)
        WarningDialog.init(this)
        LocalUtil.init(this)
        LocalUtil.loadLocal(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        FirebaseMessageReceiver.sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { firebaseToken ->
            FirebaseMessageReceiver.token = firebaseToken
            Log.d(TAG, "onCreate: $firebaseToken")
        }

        //setup navStart
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_navHost) as NavHostFragment
        navController = navHostFragment.findNavController()
        onNewIntent(intent)

        binding.apply {
            mainBn.background = null
            mainBn.menu.getItem(2).isEnabled = false
            mainBn.setupWithNavController(navController)
            mainFabHome.setOnClickListener {
                navController.popBackStack(R.id.homeFragment, false)
            }

            viewModelShared.openDrawer.observe(this@MainActivity) { isOpen ->
                if (isOpen) {
                    hideSoftKeyboard()
                    mainDrawerLayout.openDrawer(GravityCompat.START)
                    viewModelShared.openDrawer.value = false
                }
            }

            viewModelShared.profileData.observe(this@MainActivity) { member ->
                inDrawerHeader.apply {
                    navHeaderTvUserName.text = member.userName
                    UserUtil.saveUserProfile(EventsApi.IMAGE_URL_PROFILE + member.mImage!!)
                    Glide.with(this@MainActivity)
                        .load(EventsApi.IMAGE_URL_PROFILE + member.mImage)
                        .into(navHeaderIvProfile)
                }
            }
        }
        setupDrawer()
        setupNavBottomVisibility()

    }

    // Hide keyboard onTouch outside
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
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

                Constants.setupLangChooser(
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

//            navHeaderTvMyAccount.setOnClickListener {
//                navController.navigate(R.id.myAccountFragment)
//
//                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
//            }

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
                R.id.changePasswordFragment,
                R.id.editPersonalDetailsFragment,
                R.id.myAccountFragment,
                R.id.cancelDialog -> {
                    binding.apply {
                        mainBottomAppBar.visibility = View.GONE
                        mainFabHome.visibility = View.GONE
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
        }
    }

}