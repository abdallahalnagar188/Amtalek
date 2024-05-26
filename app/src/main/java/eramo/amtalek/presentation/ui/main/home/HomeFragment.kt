package eramo.amtalek.presentation.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.databinding.FragmentHomeBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.home.HomeExtraSectionsModel
import eramo.amtalek.domain.model.home.cities.CitiesModel
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.model.main.home.NewsModelx
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedRealEstateAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFifthExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFindPropertyByCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeThirdExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFirstExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFourthExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeMostViewedPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeSecondExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNormalPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.FilterCitiesDialogFragment
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromTopAnimation
import eramo.amtalek.util.onBackPressed
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(),
RvHomeFeaturedRealEstateAdapter.OnItemClickListener ,
RvHomeFeaturedProjectsAdapter.OnItemClickListener,
RvHomeFindPropertyByCityAdapter.OnItemClickListener,
    RvHomeNormalPropertiesAdapter.OnItemClickListenerNormalProperties,
    RvHomeMostViewedPropertiesAdapter.OnItemClickListenerMostViewedProperties,
RvHomeFirstExtraSectionAdapter.OnItemClickListenerFirstSection,
RvHomeSecondExtraSectionAdapter.OnItemClickListenerSecondSection,
RvHomeThirdExtraSectionAdapter.OnItemClickListenerThirdSection,
RvHomeFourthExtraSectionAdapter.OnItemClickListenerFourthSection,
RvHomeFifthExtraSectionAdapter.OnItemClickListenerFifthSection,
RvHomeNewsAdapter.OnItemClickListener{

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate

    private val viewModel by viewModels<HomeMyViewModel>()

    private val viewModelShared: SharedViewModel by activityViewModels()

    private var backPressedTime: Long = 0
    // ---------------------------------------------------------------------------

    @Inject
    lateinit var rvHomeFeaturedRealEstateAdapter: RvHomeFeaturedRealEstateAdapter

    @Inject
    lateinit var rvHomeFeaturedProjectsAdapter: RvHomeFeaturedProjectsAdapter

    @Inject
    lateinit var rvHomeFindPropertyByCityAdapter: RvHomeFindPropertyByCityAdapter

    @Inject
    lateinit var rvHomeNormalPropertiesAdapter: RvHomeNormalPropertiesAdapter

    @Inject
    lateinit var rvHomeMostViewedPropertiesAdapter: RvHomeMostViewedPropertiesAdapter

    // ---------------------------------------------------------------------------
    @Inject
    lateinit var rvHomeFirstExtraSectionAdapter: RvHomeFirstExtraSectionAdapter

    @Inject
    lateinit var rvHomeSecondExtraSectionAdapter: RvHomeSecondExtraSectionAdapter

    @Inject
    lateinit var rvHomeThirdExtraSectionAdapter: RvHomeThirdExtraSectionAdapter

    @Inject
    lateinit var rvHomeFourthExtraSectionAdapter: RvHomeFourthExtraSectionAdapter

    @Inject
    lateinit var rvHomeFifthExtraSectionAdapter: RvHomeFifthExtraSectionAdapter
    // ---------------------------------------------------------------------------

    @Inject
    lateinit var rvHomeNewsAdapter: RvHomeNewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        listeners()
        requestApis()
        fetchData()
        if (UserUtil.isUserLogin()) {
            viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
        }
    }

    override fun onResume() {
        super.onResume()

        binding.inToolbar.FHomeEtSearch.text.clear()
//        binding.root.requestFocus()
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.inToolbar.FHomeEtSearch.windowToken, 0)

//        lifecycleScope.launch {
//            delay(1000)
//        binding.inToolbar.FHomeEtSearch.clearFocus()
//        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initToolbar()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        binding.apply {

            val filterCitiesDialogFragment = FilterCitiesDialogFragment()
//            filterCitiesDialogFragment.setListener(this@HomeFragment)
            inToolbar.spinnerLayout.setOnClickListener {
                filterCitiesDialogFragment.show(
                    activity?.supportFragmentManager!!,
                    "FilterCitiesDialogFragment"
                )
            }

            inToolbar.inNotification.root.setOnClickListener {
//                findNavController().navigate(R.id.notificationFragment, null, navOptionsAnimation())
            }
            inToolbar.inMessaging.root.setOnClickListener {
//                findNavController().navigate(R.id.messagingFragment, null, navOptionsAnimation())
            }
        }

        setupSearchFunctionality()
        this@HomeFragment.onBackPressed { pressBackAgainToExist() }
    }

    private fun requestApis() {
        viewModel.getHomeApis("1")
    }

    private fun fetchData() {
        fetchGetProfileState()
        fetchUserCityState()
        fetchGetHomeApis()
        fetchGetHomeFeaturedProperties()
        fetchGetHomeProjects()
        fetchGetHomeFilterByCity()
        fetchGetHomeSlider()
        fetchGetHomeMostViewedProperties()
        fetchHomeNormalProperties()
        fetchGetHomeExtraSections()
        fetchGetNews()
    }

    // -------------------------------------- fetchData -------------------------------------- //
    private fun fetchGetHomeApis(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.initScreenState.collect(){state->
                    when (state){
                        is UiState.Success->{

                            dismissShimmerEffect()
                        }
                        is UiState.Loading->{
                            showShimmerEffect()
                        }
                        is UiState.Error->{
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }
    private fun fetchGetHomeFeaturedProperties() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeFeaturedPropertiesState.collect(){state->
                    when (state) {

                        is UiState.Success -> {
                            val data = state.data?.get(0)?.propertiesList
                            binding.inFeaturedRealEstate.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()){
                                setupFeaturedRealEstateRv(data)
                            }else{
                                binding.inFeaturedRealEstate.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }
                }
            }
        }
    }
    private fun fetchGetHomeProjects(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeProjectsState.collect(){state->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.projects
                            binding.inFeaturedProjects.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()){
                                setupFeaturedProjectsRv(data)
                            }else{
                                binding.inFeaturedProjects.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }
    private fun fetchGetHomeFilterByCity(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeFilterByCityState.collect(){state->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.cities
                            binding.inPropertiesByCity.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()){
                                setupFindPropertiesByCityRv(data)
                            }else{
                                binding.inPropertiesByCity.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }
    private fun fetchGetHomeSlider(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeSliderState.collect(){state->
                    when (state) {

                        is UiState.Success -> {
                            val data = state.data
                            if (!data.isNullOrEmpty()){
                                setupSliderBetween(parseBetweenCarouselSliderList(data))
                            }else{
                                binding.carouselSliderBetween.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
    }
    }
    private fun fetchHomeNormalProperties(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeNormalPropertiesState.collect(){state->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.propertiesList
                            binding.inNormalPropertiesLayout.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()){
                                setupNormalPropertiesRv(data)
                            }else{
                                binding.inNormalPropertiesLayout.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }
                        is UiState.Loading -> {
                        }
                        else -> {}
                    }

                }
            }
        }
    }
    private fun fetchGetHomeMostViewedProperties(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeMostViewedPropertiesState.collect(){state->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.propertiesList
                            binding.inMostViewedPropertiesLayout.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()){
                                setupMostViewedPropertiesRv(data)
                            }else{
                                binding.inMostViewedPropertiesLayout.root.visibility = View.GONE
                            }
                        }
                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }
                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }



    private fun fetchGetHomeExtraSections(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeExtraSectionsState.collect(){state->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data
                            setupExtraViews(data)
                        }
                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }
    private fun fetchGetNews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeNewsState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            val data = state.data?.get(0)?.news
                            binding.inNewsLayout.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()){
                                setupNewsRv(data)
                            }else{
                                binding.inNewsLayout.root.visibility = View.GONE
                            }

                        }

                        is UiState.Error -> {


                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }
                }
            }

        }
    }
    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            viewModelShared.profileData.value = UiState.Success(state.data!!)
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }

                }
            }
        }
    }
    private fun fetchUserCityState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelShared.profileData.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            binding.inToolbar.tvSpinnerText.text = state.data?.cityName
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }

                }
            }
        }
    }
    //------------------------------------------------------------------------------------------//
    private fun parseBetweenCarouselSliderList(data: List<SliderModel>?): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        for (i in data!!) {
            list.add(
                CarouselItem(
                    imageUrl = i?.image,
                )
            )
        }

        return list
    }

    private fun setupSliderBetween(data: ArrayList<CarouselItem>) {
        binding.apply {
            carouselSliderBetween.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderBetween.setData(data)
            carouselSliderBetween.setIndicator(carouselSliderBetweenDots)
            carouselSliderBetween.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
//                    return ItemAdsBinding.inflate(
                    return ItemSliderTopBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
//                    val currentBinding = binding as ItemAdsBinding
                    val currentBinding = binding as ItemSliderTopBinding
                    currentBinding.apply {
                        imageView13.setImage(item)
                        this.root.setOnClickListener {
                            this@HomeFragment.binding.inToolbar.FHomeEtSearch.clearFocus()
                        }
                    }


                }
            }
        }
    }

    private fun initToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
    }

    private fun setupFeaturedRealEstateRv(data: List<PropertyModel>) {
        rvHomeFeaturedRealEstateAdapter.setListener(this@HomeFragment)
        binding.inFeaturedRealEstate.rv.adapter = rvHomeFeaturedRealEstateAdapter
        rvHomeFeaturedRealEstateAdapter.submitList(data)
        binding.inFeaturedRealEstate.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupFeaturedProjectsRv(data: List<ProjectModel>) {
        rvHomeFeaturedProjectsAdapter.setListener(this@HomeFragment)
        binding.inFeaturedProjects.rv.adapter = rvHomeFeaturedProjectsAdapter
        rvHomeFeaturedProjectsAdapter.submitList(data)
        binding.inFeaturedProjects.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))
    }

    private fun setupFindPropertiesByCityRv(data: List<CitiesModel>) {
        rvHomeFindPropertyByCityAdapter.setListener(this@HomeFragment)
        binding.inPropertiesByCity.rv.adapter = rvHomeFindPropertyByCityAdapter
        rvHomeFindPropertyByCityAdapter.submitList(data)
        binding.inPropertiesByCity.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }
    private fun setupNormalPropertiesRv(data: List<PropertyModel>) {
        rvHomeNormalPropertiesAdapter.setListener(this@HomeFragment)
        binding.inNormalPropertiesLayout.rv.adapter = rvHomeNormalPropertiesAdapter
        rvHomeNormalPropertiesAdapter.submitList(data)
        binding.inNormalPropertiesLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }
    private fun setupMostViewedPropertiesRv(data: List<PropertyModel>) {
        rvHomeMostViewedPropertiesAdapter.setListener(this@HomeFragment)
        binding.inMostViewedPropertiesLayout.rv.adapter = rvHomeMostViewedPropertiesAdapter
        rvHomeMostViewedPropertiesAdapter.submitList(data)
        binding.inMostViewedPropertiesLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }
    private fun setupExtraViews(data: List<HomeExtraSectionsModel>?) {
        ////// first
        binding.inFirstExtraSectionLayout.tvTitle.text = data?.get(0)?.title
        if (data?.get(0)?.sections?.isNotEmpty() == true){
            setupHomeFirstExtraSectionRv(data[0].sections!!)

        }else{
            binding.inFirstExtraSectionLayout.root.visibility = View.GONE
        }
        ///// second
        binding.inSecondExtraSectionLayout.tvTitle.text = data?.get(1)?.title
        if (data?.get(1)?.sections?.isNotEmpty() == true){
            setupHomeSecondExtraSectionRv(data[1].sections!!)

        }else{
            binding.inSecondExtraSectionLayout.root.visibility = View.GONE
        }
        ///// third
        binding.inThirdExtraSectionLayout.tvTitle.text = data?.get(2)?.title
        if (data?.get(2)?.sections?.isNotEmpty()== true){
            setupHomeThirdExtraSectionRv(data[2].sections!!)

        }else{
            binding.inThirdExtraSectionLayout.root.visibility = View.GONE
        }
        ///// fourth
        binding.inFourthExtraSectionLayout.tvTitle.text = data?.get(3)?.title
        if (data?.get(3)?.sections?.isNotEmpty()== true){
            setupHomeFourthExtraSectionRv(data[3].sections!!)

        }else{
            binding.inFourthExtraSectionLayout.root.visibility = View.GONE
        }
        ///// fifth
        binding.inFifthExtraSectionLayout.tvTitle.text = data?.get(4)?.title
        if (data?.get(4)?.sections?.isNotEmpty()== true){
            setupHomeFifthExtraSectionRv(data[4].sections!!)

        }else{
            binding.inFifthExtraSectionLayout.root.visibility = View.GONE
        }
    }
    // -------------------------------------------------------------------------------------------------------------- //
    private fun setupHomeFirstExtraSectionRv(data: List<PropertyModel>) {
        rvHomeFirstExtraSectionAdapter.setListener(this)
        binding.inFirstExtraSectionLayout.rv.adapter = rvHomeFirstExtraSectionAdapter
        rvHomeFirstExtraSectionAdapter.submitList(data)
        binding.inFirstExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupHomeSecondExtraSectionRv(data: List<PropertyModel>) {
        rvHomeSecondExtraSectionAdapter.setListener(this)
        binding.inSecondExtraSectionLayout.rv.adapter = rvHomeSecondExtraSectionAdapter
        rvHomeSecondExtraSectionAdapter.submitList(data)
        binding.inSecondExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupHomeThirdExtraSectionRv(data: List<PropertyModel>) {
        rvHomeThirdExtraSectionAdapter.setListener(this)
        binding.inThirdExtraSectionLayout.rv.adapter = rvHomeThirdExtraSectionAdapter
        rvHomeThirdExtraSectionAdapter.submitList(data)
        binding.inThirdExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }
    private fun setupHomeFourthExtraSectionRv(data: List<PropertyModel>) {
        rvHomeFourthExtraSectionAdapter.setListener(this)
        binding.inFourthExtraSectionLayout.rv.adapter = rvHomeFourthExtraSectionAdapter
        rvHomeFourthExtraSectionAdapter.submitList(data)
        binding.inFourthExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }
    private fun setupHomeFifthExtraSectionRv(data: List<PropertyModel>) {
        rvHomeFifthExtraSectionAdapter.setListener(this)
        binding.inFifthExtraSectionLayout.rv.adapter = rvHomeFifthExtraSectionAdapter
        rvHomeFifthExtraSectionAdapter.submitList(data)
        binding.inFifthExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupNewsRv(data: List<NewsModel>) {
        rvHomeNewsAdapter.setListener(this@HomeFragment)
        binding.inNewsLayout.rv.adapter = rvHomeNewsAdapter
        rvHomeNewsAdapter.submitList(data)
        binding.inNewsLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchFunctionality() {
        binding.inToolbar.apply {

            // Search action
            FHomeEtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = FHomeEtSearch.text.toString().trim()

                    if (query.isEmpty()) {
                        showToast(getString(R.string.enter_a_query))
                    } else {
                        findNavController().navigate(
                            R.id.searchPropertyResultFragment,
                            null,
                            navOptionsFromTopAnimation()
                        )
                    }

                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(500)
                        FHomeEtSearch.clearFocus()
                    }
                }
                true
            }

            // onTextChange listener
//            FHomeEtSearch.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                }
//
//                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
//                    if (charSequence?.isNotEmpty() == true) {
//                        tvSearch.visibility = View.VISIBLE
//                    } else {
//                        tvSearch.visibility = View.GONE
//                    }
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//
//                }
//            })

            // onFocus listener
            FHomeEtSearch.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    tvSearch.visibility = View.VISIBLE
                } else {
                    tvSearch.visibility = View.GONE
                }
            }

            // tvSearch listener
            tvSearch.setOnClickListener {
                findNavController().navigate(
                    R.id.searchPropertyResultFragment,
                    null,
                    navOptionsFromTopAnimation()
                )
            }
        }

        // remove focus onOutsideClick
        binding.root.setOnTouchListener { _, _ ->
            binding.inToolbar.FHomeEtSearch.clearFocus()
            false
        }

        binding.scrollView.setOnTouchListener { _, _ ->
            binding.inToolbar.FHomeEtSearch.clearFocus()
            false
        }
    }

    private fun isTouchInsideView(event: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = event.rawX
        val y = event.rawY
        return x > location[0] && x < location[0] + view.width && y > location[1] && y < location[1] + view.height
    }

    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            viewLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
        }
    }

    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
        }
    }

    private fun pressBackAgainToExist() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            requireActivity().finish()
        } else {
            showToast(getString(R.string.press_back_again_to_exist))
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onFeaturedRealEstateClick(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }

    override fun onFeaturedProjectClick(model: ProjectModel) {
        findNavController().navigate(
            R.id.myProjectDetailsFragment,
            MyProjectDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation()
        )    }

    override fun onPropertyByCityClick(model: CitiesModel) {
        Toast.makeText(requireContext(), model.title, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClicked1(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation()
        )    }

    override fun onItemClicked2(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }
    override fun onItemClicked3(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }
    override fun onItemClicked4(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }
    override fun onItemClicked5(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }

    override fun onNewsClick(model: NewsModel) {
        findNavController().navigate(R.id.newsDetailsFragment,
            NewsDetailsFragmentArgs(model).toBundle(), navOptionsAnimation()
            )
    }

    override fun onMostViewedPropertiesClicked(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }

    override fun onNormalPropertyClicked(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }
    // ------------------------------------------------------------------------------------------------------------------------------------ //


}