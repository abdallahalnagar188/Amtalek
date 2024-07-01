package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentHotOffersSellBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersSellProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersSellPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HotOffersSellFragment : BindingFragment<FragmentHotOffersSellBinding>(),RvHotOffersSellPropertiesAdapter.OnItemClickListener,RvHotOffersSellProjectsAdapter.OnItemClickListener,
FavClickListener{

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersSellBinding::inflate
    private val hotOffersViewModel by viewModels<HotOffersViewModel>()
    @Inject
    lateinit var rvHotOffersSellPropertiesAdapter: RvHotOffersSellPropertiesAdapter

    @Inject
    lateinit var rvHotOffersSellProjectsAdapter: RvHotOffersSellProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Fragment", "onViewCreated")
        setupViews()
        setupObservers()
//        fetchGetHotOffers()
        setupCarouselSlider()
    }

    private fun setupObservers() {
        hotOffersViewModel.forSellListState.observe(viewLifecycleOwner){
            Log.e("observe", it.toString(), )
            rvHotOffersSellPropertiesAdapter.submitList(it)
            binding.rvProperties.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe))
            binding.carouselSliderForSell.visibility = View.VISIBLE

        }
        hotOffersViewModel.projectsListState.observe(viewLifecycleOwner){
            rvHotOffersSellProjectsAdapter.submitList(it)
            binding.rvProjects.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe))

        }
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onPause() {
        super.onPause()
    }
    private fun setupViews() {
        binding.rvProperties.adapter = rvHotOffersSellPropertiesAdapter
        binding.rvProjects.adapter = rvHotOffersSellProjectsAdapter
        rvHotOffersSellProjectsAdapter.setListener(this@HotOffersSellFragment)
        rvHotOffersSellPropertiesAdapter.setListener(this@HotOffersSellFragment,this)
    }
//    private fun fetchGetHotOffers() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
//                hotOffersViewModel.hotOffers.collect(){
//                    when(it){
//                        is UiState.Success->{
//
//                            dismissShimmerEffect()
//                        }
//                        is UiState.Error->{
//
//                            dismissShimmerEffect()
//                        }
//                        is UiState.Loading->{
//                            showShimmerEffect()
//                        }
//                        else -> {}
//                    }
//                }
//            }
//        }
//    }

    private fun setupCarouselSlider() {
        binding.apply {
            carouselSliderForSell.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderForSell.setData(Dummy.dummyCarouselList())

            carouselSliderForSell.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
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
                    val currentBinding = binding as ItemSliderTopBinding
                    currentBinding.apply {
                        imageView13.setImage(item)
                    }

                }
            }
        }
    }
    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            root.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
        }
    }
    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            root.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
        }
    }

    override fun onProjectClick(model: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel) {
        findNavController().navigate(
            R.id.myProjectDetailsFragment,
            MyProjectDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation()
        )
    }

    override fun onPropertyClick(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())
    }

    override fun onFavClick(model: PropertyModel) {
        hotOffersViewModel.addOrRemoveFav(model.id)
    }

}