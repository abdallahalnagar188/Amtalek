package eramo.amtalek.presentation.ui.social

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.Favorite
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.MyProp
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.OffersItem
import eramo.amtalek.databinding.FragmentMyProfileBinding
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.main.market.MarketPostsModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMyProfilePostsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeMostViewedPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.profile.RvSubmittedOffersAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.extension.imagviewer.ImagesListFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.offers.HotOffersViewModel
import eramo.amtalek.presentation.viewmodel.social.MyProfileViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromTopAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : BindingFragment<FragmentMyProfileBinding>(), RvHomeMostViewedPropertiesAdapter.OnItemClickListenerMostViewedProperties,
    FavClickListener, RvSubmittedOffersAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyProfileBinding::inflate

    private val viewModel by viewModels<MyProfileViewModel>()
    private val hotOffersViewModel by viewModels<HotOffersViewModel>()
    var LIKEFLAG: Int = 0


    @Inject
    lateinit var rvFavPropertiesAdapter: RvHomeMostViewedPropertiesAdapter /// because its the same usage at home fragment so no need to create a new one


    @Inject
    lateinit var rvSubmittedOffersAdapter: RvSubmittedOffersAdapter

    @Inject
    lateinit var rvMyPropertiesAdapter: RvHomeMostViewedPropertiesAdapter /// because its the same usage at home fragment so no need to create a new one




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkActorType()
        setupViews()
        listeners()
        fetchData()
        setupToggle()
        Log.e("dash", UserUtil.getDashboardLink()!! )

    }

    private fun checkActorType() {
        if (UserUtil.getUserType()=="user"){
            binding.btnDashboard.visibility = View.GONE
            requestData()
        }else if (UserUtil.getUserType()=="broker"){
            binding.tvUserName.text = UserUtil.getBrokerName()
            binding.tvLocation.text = UserUtil.getUserEmail()
            binding.btnDashboard.visibility = View.VISIBLE
            binding.ivEdit.visibility = View.GONE
            binding.userToggleGroup.visibility = View.GONE
            binding.favRv.visibility = View.GONE
            binding.submittedOfferRv.visibility = View.GONE
            binding.myPropertiesRv.visibility = View.GONE
        }
    }

    private fun setupToggle() {
        binding.userToggleGroup.addOnButtonCheckedListener{ _, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
                    R.id.fav_btn -> {
                        binding.apply {
                            favBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            favBtn.setTextColor(context?.getColor(R.color.white)!!)
                            submitedOfferBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            submitedOfferBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            myPropBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            myPropBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            submittedOfferRv.visibility = View.GONE
                            favRv.visibility = View.VISIBLE
                            myPropertiesRv.visibility = View.GONE

                        }
                    }
                    R.id.submited_offer_btn -> {
                        binding.apply {
                            submitedOfferBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            submitedOfferBtn.setTextColor(context?.getColor(R.color.white)!!)
                            favBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            favBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            myPropBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            myPropBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            submittedOfferRv.visibility = View.VISIBLE
                            favRv.visibility = View.GONE
                            myPropertiesRv.visibility = View.GONE

                        }
                    }
                    R.id.my_prop_btn -> {
                        binding.apply {
                            myPropBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            myPropBtn.setTextColor(context?.getColor(R.color.white)!!)
                            favBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            favBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            submitedOfferBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            submitedOfferBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            submittedOfferRv.visibility = View.GONE
                            favRv.visibility = View.GONE
                            myPropertiesRv.visibility = View.VISIBLE

                        }
                    }

                }
            }
        }
        binding.favBtn.isChecked =true
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        StatusBarUtil.transparent()
    }

    private fun listeners() {
        binding.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            ivEdit.setOnClickListener {
                findNavController().navigate(
                    R.id.myAccountFragment,
                    null,
                    navOptionsFromTopAnimation()
                )
            }
        }
    binding.btnDashboard.setOnClickListener {
        val url = UserUtil.getDashboardLink()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent,)
    }
    }

    private fun requestData() {
        viewModel.getProfile(UserUtil.getUserType(),UserUtil.getUserId())
    }

    private fun fetchData() {
        fetchGetProfileState()
        fetchLikeState()
    }

    private fun fetchLikeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                hotOffersViewModel.favState.collect {
                    when (it) {
                        is UiState.Success -> {
                            requestData()
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            showToast(it.message!!.asString(requireContext()))
                        }
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }
                        else -> {

                        }
                        }
                }
            }
        }
    }

    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            assignUserData(state.data!!)
                            assignViewsData(state.data)
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            if (LIKEFLAG ==1){
                                // show no shimmer effect but loading one
                            }else{
                                showShimmerEffect()

                            }
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun assignViewsData(data: ProfileModel) {
        initRvMyFavList(data.favList)
        initRvSubmittedOffersList(data.offers)
        initRvMyPropertiesList(data.myProperties)
    }

    private fun initRvMyPropertiesList(myProperties: List<MyProp>) {
        val data = ArrayList<PropertyModel>()
        for(item in myProperties){
            data.add(item.toPropertyModel())
        }
        binding.myPropertiesRv.adapter = rvMyPropertiesAdapter
        rvMyPropertiesAdapter.submitList(data)
        rvMyPropertiesAdapter.setListener(this, this)
    }

    private fun initRvSubmittedOffersList(offers: List<OffersItem>) {
        val data = ArrayList<PropertyModel>()
        for(item in offers){
            data.add(item.toProperty())
        }
        binding.submittedOfferRv.adapter = rvSubmittedOffersAdapter
        rvSubmittedOffersAdapter.submitList(data)
        rvSubmittedOffersAdapter.setListener(this, this)
    }

    private fun initRvMyFavList(favList: List<Favorite>) {
        val proptiesList = ArrayList<PropertyModel>()
        for(item in favList){
            proptiesList.add(item.toProperty())
        }
        binding.favRv.adapter = rvFavPropertiesAdapter
        rvFavPropertiesAdapter.submitList(proptiesList)
        rvFavPropertiesAdapter.setListener(this, this)
    }

    private fun assignUserData(user: ProfileModel) {
        try {
            binding.apply {
                tvUserName.text = getString(R.string.S_user_name, user.firstName, user.lastName)
                tvLocation.text = user.cityName
                tvBio.text = user.bio

//                if (user.cover != "") {
//                    Glide.with(requireContext()).load(user.cover).into(ivUserCover)
//                }

                if (user.image != "") {
                    Glide.with(requireContext()).load(user.image).into(ivUserProfile)
                } else {
                    Glide.with(requireContext()).load(R.drawable.avatar).into(ivUserProfile)
                }
            }
            dismissShimmerEffect()

        } catch (e: Exception) {
            dismissShimmerEffect()
            showToast(getString(R.string.invalid_data_parsing))
        }
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



    override fun onMostViewedPropertiesClicked(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())
    }

    override fun onFeaturedRealEstateClick(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }

    override fun onFavClick(model: PropertyModel) {
        hotOffersViewModel.addOrRemoveFav(model.id)
        LIKEFLAG = 1
    }
}