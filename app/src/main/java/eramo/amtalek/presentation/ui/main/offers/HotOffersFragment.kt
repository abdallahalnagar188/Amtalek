package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.hotoffers.HotOffersResponse
import eramo.amtalek.databinding.FragmentHotOffersBinding
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.DummyProjectAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersForBothProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersForBothPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersSellPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromTopAnimation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class HotOffersFragment : BindingFragment<FragmentHotOffersBinding>(),
    RvHotOffersForBothPropertiesAdapter.OnItemClickListener,
    RvHotOffersRentPropertiesAdapter.OnItemClickListener,
    RvHotOffersSellPropertiesAdapter.OnItemClickListener,
    RvHotOffersForBothProjectsAdapter.OnItemClickListener,
        FavClickListener
{

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()

    private val hotOffersViewModel: HotOffersViewModel by viewModels()
    private val forSellPropertyList = ArrayList<PropertyModel>()
    private val forRentPropertyList:ArrayList<PropertyModel> = ArrayList()
    private val forBothPropertyList:ArrayList<PropertyModel> = ArrayList()
    private val projectsList:ArrayList<ProjectModel> = ArrayList()

    @Inject
    lateinit var rvHotOffersForBothPropertiesAdapter: RvHotOffersForBothPropertiesAdapter

    @Inject
    lateinit var rvHotOffersForBothProjectsAdapter: RvHotOffersForBothProjectsAdapter

    @Inject
    lateinit var rvHotOffersSellPropertiesAdapter: RvHotOffersSellPropertiesAdapter



    @Inject
    lateinit var rvHotOffersRentPropertiesAdapter: RvHotOffersRentPropertiesAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotOffersViewModel.getHotOffers(UserUtil.getUserCountryFiltrationTitleId())
        setUpObservers()
        setupViews()
        listeners()
        bindTabs()
        tabsClickListeners()
        handleRefresh()
    }

    private fun handleRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {


            rvHotOffersForBothPropertiesAdapter.submitList(null)

            rvHotOffersSellPropertiesAdapter.submitList(null)

            rvHotOffersRentPropertiesAdapter.submitList(null)

            rvHotOffersForBothProjectsAdapter.submitList(null)

            binding.swipeRefreshLayout.isRefreshing = false
            binding.carouselSlider.visibility = View.GONE
            hotOffersViewModel.getHotOffers(UserUtil.getUserCountryFiltrationTitleId())

        }
    }

    private fun listeners() {
        binding.inToolbar.spinnerLayout.setOnClickListener {
            findNavController().navigate(R.id.filterCitiesDialogFragment, null, navOptionsAnimation())
        }

    }
    private fun initToolbar() {
        binding.inToolbar.apply {

            FHomeEtSearch.visibility = View.GONE
            ivSearch.visibility =  View.GONE
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
            inMessaging.root.setOnClickListener{
                findNavController().navigate(R.id.messagingChatFragment)
            }
        }
        if (LocalUtil.isEnglish()){
            binding.inToolbar.tvSpinnerText.text = UserUtil.getCityFiltrationTitleEn()

        }else{
            binding.inToolbar.tvSpinnerText.text = UserUtil.getCityFiltrationTitleAr()
        }
        if (UserUtil.getCityFiltrationTitleAr().isEmpty()&&UserUtil.getCityFiltrationTitleEn().isEmpty()){
            binding.inToolbar.tvSpinnerText.text = context?.getString(R.string.select_city)

        }
        if (LocalUtil.isEnglish()){
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))

        }else{
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }
    }

    private fun setupViews() {
        initToolbar()

        binding.rvPropertiesForBoth.adapter = rvHotOffersForBothPropertiesAdapter
        binding.rvPropertiesForSale.adapter = rvHotOffersSellPropertiesAdapter
        binding.rvPropertiesForRent.adapter = rvHotOffersRentPropertiesAdapter
        binding.rvProjectsForBoth.adapter = rvHotOffersForBothProjectsAdapter

        rvHotOffersRentPropertiesAdapter.setListener(this@HotOffersFragment,this)
        rvHotOffersSellPropertiesAdapter.setListener(this@HotOffersFragment,this)
        rvHotOffersForBothPropertiesAdapter.setListener(this@HotOffersFragment,this)
        rvHotOffersForBothProjectsAdapter.setListener(this@HotOffersFragment)

    }
    private fun setUpObservers() {
        fetchGetHotOffers()
    }


    private fun fetchGetHotOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                hotOffersViewModel.hotOffers.collect(){
                    when(it){
                        is UiState.Success->{

                            forSellPropertyList.clear()
                            forRentPropertyList.clear()
                            forBothPropertyList.clear()
                            projectsList.clear()

                            filterProperties(it.data)
                        }
                        is UiState.Error->{
                            dismissShimmerEffect()
                        }
                        is UiState.Loading->{
                            showShimmerEffect()
                        }
                        else -> {}
                    }
                }
            }
        }
    }



    private fun bindTabs() {
        val tab = binding.tabLayout.newTab()
        tab.text = getString(R.string.for_sell)
        tab.setId(0)
        binding.tabLayout.addTab(tab)

        val tab2 = binding.tabLayout.newTab()
        tab2.text = getString(R.string.for_rent)
        tab2.setId(1)
        binding.tabLayout.addTab(tab2)

        val tab3 = binding.tabLayout.newTab()
        tab3.text = getString(R.string.forBoth)
        tab3.setId(2)
        binding.tabLayout.addTab(tab3)

        for (i in 0 until binding.tabLayout.tabCount) {
            val tabs = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tabs.layoutParams as ViewGroup.MarginLayoutParams
//            p.setMargins(0, 0, 50, 0)
            p.marginEnd = 25
            tabs.requestLayout()
        }
        binding.tabLayout.getTabAt(0)?.select()
    }
    private fun tabsClickListeners() {
        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val tabIndex = tab?.position
//                    if (tabIndex != null) {
//                        binding.tabLayout.setScrollPosition(tabIndex, 0f, true)
//                    }
                    when (tabIndex) {
                        2 -> {
                            binding.rvPropertiesForBoth.visibility = View.VISIBLE
                            binding.rvPropertiesForSale.visibility = View.GONE
                            binding.rvPropertiesForRent.visibility = View.GONE
                        }
                        0 -> {
                            binding.rvPropertiesForBoth.visibility = View.GONE
                            binding.rvPropertiesForSale.visibility = View.VISIBLE
                            binding.rvPropertiesForRent.visibility = View.GONE
                        }
                        1 -> {
                            binding.rvPropertiesForBoth.visibility = View.GONE
                            binding.rvPropertiesForSale.visibility = View.GONE
                            binding.rvPropertiesForRent.visibility = View.VISIBLE
                        }
                    }

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }
            }
        )
    }

    private fun filterProperties(data: HotOffersResponse?) {
        for (project in data?.data?.projects!!){
            if (project != null) {
                projectsList.add(project.toProjectModel())
            }
        }
        Log.e("filterProperties: ", "Started", )
        var counter = 0
        for (property in data.data?.properties!!){
            counter++
            when (property?.forWhat) {
                "for_sale" -> {
                    if (forSellPropertyList.size < 10){
                        forSellPropertyList.add(property.toPropertyModel()) /////////// till u apply paging on it, cuz the performance is so poor and many many data will be loaded
                    }
                }
                "for_rent" -> {
                    if (forRentPropertyList.size < 10){
                        forRentPropertyList.add(property.toPropertyModel()) /////////// till u apply paging on it, cuz the performance is so poor and many many data will be loaded
                    }
                }
                "for_both" -> {
                    if (forBothPropertyList.size < 10){
                        forBothPropertyList.add(property.toPropertyModel()) /////////// till u apply paging on it, cuz the performance is so poor and many many data will be loaded
                    }
                }
            }
        }
        Log.e("filterProperties: ", "Finished", )

        binding.carouselSlider.visibility = View.VISIBLE



        rvHotOffersForBothPropertiesAdapter.submitList(forBothPropertyList)

        rvHotOffersSellPropertiesAdapter.submitList(forSellPropertyList)

        rvHotOffersRentPropertiesAdapter.submitList(forRentPropertyList)

        rvHotOffersForBothProjectsAdapter.submitList(projectsList)
        dismissShimmerEffect()

//        binding.rvProjectsForBoth.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe))
    }

    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            shimmerLayout.visibility = View.VISIBLE
        }
    }
    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
        }
  }

    override fun onProjectClick(model: ProjectModel) {
        findNavController().navigate(
            R.id.myProjectDetailsFragment,
            MyProjectDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onPropertyClickForRent(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle())
    }


    override fun onPropertyClickForBoth(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle())    }

    override fun onPropertyClickForSale(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onFavClick(model: PropertyModel) {
        hotOffersViewModel.addOrRemoveFav(model.id)
    }
}