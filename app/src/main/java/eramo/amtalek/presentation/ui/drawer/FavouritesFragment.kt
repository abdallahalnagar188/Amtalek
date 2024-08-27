package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.Favorite
import eramo.amtalek.databinding.FragmentFavouritesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeMostViewedPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.offers.HotOffersViewModel
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.social.MyProfileViewModel
import eramo.amtalek.util.Dummy.dummyMyFavouritesList
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>(),
    RvHomeMostViewedPropertiesAdapter.OnItemClickListenerMostViewedProperties,
    FavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentFavouritesBinding::inflate

    @Inject
    lateinit var rvFavPropertiesAdapter: RvHomeMostViewedPropertiesAdapter

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel by viewModels<MyProfileViewModel>()
    private val hotOffersViewModel by viewModels<HotOffersViewModel>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
        setupViews()
        fetchGetProfileState()
    }

    private fun setupViews() {
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
    }

    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            state.data?.let { assignViewsData(it) }
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }
    private fun assignViewsData(data: ProfileModel) {
        initRvMyFavList(data.favList)

    }
    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.my_favourite_properties)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRvMyFavList(favList: List<Favorite>) {
        val proptiesList = ArrayList<PropertyModel>()
        for(item in favList){
            proptiesList.add(item.toProperty())
        }
        binding.rvProperties.adapter = rvFavPropertiesAdapter
        rvFavPropertiesAdapter.submitList(proptiesList)
        rvFavPropertiesAdapter.setListener(this, this)
    }


    override fun onMostViewedPropertiesClicked(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation()
        )

    }

    override fun onFavClick(model: PropertyModel) {
        hotOffersViewModel.addOrRemoveFav(model.id)
    }

}