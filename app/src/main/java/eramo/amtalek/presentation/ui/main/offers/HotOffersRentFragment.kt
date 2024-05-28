package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentHotOffersRentBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HotOffersRentFragment : BindingFragment<FragmentHotOffersRentBinding>(),RvHotOffersRentPropertiesAdapter.OnItemClickListener,RvHotOffersRentProjectsAdapter.OnItemClickListener,FavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersRentBinding::inflate

    private val hotOffersViewModel by viewModels<HotOffersViewModel>()

    @Inject
    lateinit var rvHotOffersRentPropertiesAdapter: RvHotOffersRentPropertiesAdapter

    @Inject
    lateinit var rvHotOffersRentProjectsAdapter: RvHotOffersRentProjectsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        fetchGetHotOffers()
        setupCarouselSlider()
    }

    private fun setupObservers() {
        hotOffersViewModel.forRentListState.observe(viewLifecycleOwner){
            rvHotOffersRentPropertiesAdapter.submitList(it)
            binding.rvPropertiesForRent.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe))

        }
        hotOffersViewModel.projectsListState.observe(viewLifecycleOwner){
            rvHotOffersRentProjectsAdapter.submitList(it)
            binding.rvProjects.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe))

        }
    }

    override fun onResume() {
        super.onResume()
        hotOffersViewModel.getHotOffers()

    }
    override fun onPause() {
        super.onPause()
    }
    private fun fetchGetHotOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                hotOffersViewModel.hotOffers.collect(){
                    when(it){
                        is UiState.Success->{

                            dismissShimmerEffect()
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

    private fun setupViews() {
        binding.rvPropertiesForRent.adapter = rvHotOffersRentPropertiesAdapter
        binding.rvProjects.adapter = rvHotOffersRentProjectsAdapter
        rvHotOffersRentProjectsAdapter.setListener(this)
        rvHotOffersRentPropertiesAdapter.setListener(this,this)
    }
    private fun setupCarouselSlider() {
        binding.apply {
            carouselSliderForRent.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderForRent.setData(Dummy.dummyCarouselList())

            carouselSliderForRent.carouselListener = object : CarouselListener {
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

    override fun onPropertyClick(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())    }

    override fun onProjectClick(model: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel) {
        findNavController().navigate(R.id.myProjectDetailsFragment,MyProjectDetailsFragmentArgs(model.listingNumber).toBundle(), navOptionsAnimation())
    }

    override fun onFavClick(model: PropertyModel) {
        hotOffersViewModel.addOrRemoveFav(model.id)

    }
}