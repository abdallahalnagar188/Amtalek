package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentHotOffersSellBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersSellProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersSellPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HotOffersSellFragment : BindingFragment<FragmentHotOffersSellBinding>(),RvHotOffersSellPropertiesAdapter.OnItemClickListener,RvHotOffersSellProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersSellBinding::inflate

    @Inject
    lateinit var rvHotOffersSellPropertiesAdapter: RvHotOffersSellPropertiesAdapter

    @Inject
    lateinit var rvHotOffersSellProjectsAdapter: RvHotOffersSellProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        initPropertiesRv(Dummy.dummyMyFavouritesList(requireContext()))
        initProjectsRv(Dummy.dummyProjectsList(requireContext()))
        initViews()
        setupCarouselSlider()
    }

    private fun initViews() {
        rvHotOffersSellProjectsAdapter.setListener(this@HotOffersSellFragment)
        rvHotOffersSellPropertiesAdapter.setListener(this@HotOffersSellFragment)
    }

    private fun initPropertiesRv(data: List<MyFavouritesModel>) {
        binding.rvProperties.adapter = rvHotOffersSellPropertiesAdapter
        rvHotOffersSellPropertiesAdapter.submitList(data)
    }

    private fun initProjectsRv(data: List<ProjectModel>) {
        binding.rvProjects.adapter = rvHotOffersSellProjectsAdapter
        rvHotOffersSellProjectsAdapter.submitList(data)
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

    override fun onPropertyClick(model: ProjectModel) {
        Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
    }

    override fun onPropertyClick(model: MyFavouritesModel) {
        Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
    }

}