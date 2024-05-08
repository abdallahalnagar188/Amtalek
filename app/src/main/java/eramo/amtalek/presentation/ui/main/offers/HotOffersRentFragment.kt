package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentHotOffersRentBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HotOffersRentFragment : BindingFragment<FragmentHotOffersRentBinding>(),RvHotOffersRentPropertiesAdapter.OnItemClickListener,RvHotOffersRentProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersRentBinding::inflate


    @Inject
    lateinit var rvHotOffersRentPropertiesAdapter: RvHotOffersRentPropertiesAdapter

    @Inject
    lateinit var rvHotOffersRentProjectsAdapter: RvHotOffersRentProjectsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        setupViews()
    }

    private fun clickListeners() {
    }

    private fun setupViews() {
        initPropertiesRv(Dummy.dummyMyFavouritesList(requireContext()))
        initProjectsRv(Dummy.dummyProjectsList(requireContext()))
        setupCarouselSlider()
    }

    private fun initPropertiesRv(data: List<MyFavouritesModel>) {
        binding.rvProperties.adapter = rvHotOffersRentPropertiesAdapter
        rvHotOffersRentPropertiesAdapter.setListener(this)
        rvHotOffersRentPropertiesAdapter.submitList(data)
    }

    private fun initProjectsRv(data: List<ProjectModel>) {
        binding.rvProjects.adapter = rvHotOffersRentProjectsAdapter
        rvHotOffersRentProjectsAdapter.submitList(data)
        rvHotOffersRentProjectsAdapter.setListener(this)
    }

    private fun setupCarouselSlider() {
        binding.apply {
            carouselSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSlider.setData(Dummy.dummyCarouselList())

            carouselSlider.carouselListener = object : CarouselListener {
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

    override fun onPropertyClick(model: MyFavouritesModel) {
        Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
    }

    override fun onPropertyClick(model: ProjectModel) {
        Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
    }
}