package eramo.amtalek.presentation.ui.main.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchPropertyResultBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersRentPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.FilterDialogFragment
import eramo.amtalek.presentation.ui.dialog.SortDialogFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.onBackPressed
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class SearchPropertyResultFragment : BindingFragment<FragmentSearchPropertyResultBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchPropertyResultBinding::inflate

    @Inject
    lateinit var rvHotOffersRentPropertiesAdapter: RvHotOffersRentPropertiesAdapter

    @Inject
    lateinit var rvHotOffersRentProjectsAdapter: RvHotOffersRentProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        setupToolbar()

        initPropertiesRv(Dummy.dummyMyFavouritesList(requireContext()))
        initProjectsRv(Dummy.dummyProjectsList(requireContext()))

        setupCarouselSlider()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {

            tvAdvancedSearch.text = getString(R.string.search_results)
            ivClose.setOnClickListener {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    private fun listeners() {
        binding.apply {
            sortLayout.setOnClickListener {

                val sortDialogFragment = SortDialogFragment()
                sortDialogFragment.show(
                    activity?.supportFragmentManager!!,
                    "sortDialogFragment"
                )
            }

            btnFilter.setOnClickListener {
                val filterDialogFragment = FilterDialogFragment()

                filterDialogFragment.show(
                    activity?.supportFragmentManager!!,
                    "filterDialogFragment"
                )
            }
        }
        this@SearchPropertyResultFragment.onBackPressed {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private fun initPropertiesRv(data: List<MyFavouritesModel>) {
        binding.rvProperties.adapter = rvHotOffersRentPropertiesAdapter
        rvHotOffersRentPropertiesAdapter.submitList(data)
    }

    private fun initProjectsRv(data: List<ProjectModel>) {
        binding.rvProjects.adapter = rvHotOffersRentProjectsAdapter
        rvHotOffersRentProjectsAdapter.submitList(data)
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
}