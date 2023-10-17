package eramo.amtalek.presentation.ui.main.extension.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsBinding
import eramo.amtalek.databinding.ItemAdsBinding
import eramo.amtalek.presentation.adapters.BannerSliderAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyAlbumAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyCommentAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyPropertyAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.MapFragment
import eramo.amtalek.presentation.ui.SliderZoomFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.CartViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class PropertyDetailsFragment : BindingFragment<FragmentPropertyDetailsBinding>(),
    BannerSliderAdapter.OnItemClickListener,
    DummyAlbumAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var dummyAlbumAdapter: DummyAlbumAdapter

    @Inject
    lateinit var dummyPropertyAdapter: DummyPropertyAdapter

    @Inject
    lateinit var dummyCommentAdapter: DummyCommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiRequest { viewModel.cartData() }
        super.registerApiCancellation { viewModel.cancelRequest() }

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        setupSlider()
        setupAlbum()
        setupAdsSlider()
        setupSummaryTab()
        binding.apply {
            tvSummary.setOnClickListener { setupSummaryTab() }
            tvLocation.setOnClickListener { setupMapTab() }
            tvBroker.setOnClickListener { setupBrokerContactTab() }

            dummyPropertyAdapter.submitList(Dummy.list())
            rvProperties.adapter = dummyPropertyAdapter

            dummyCommentAdapter.submitList(Dummy.list())
            rvComments.adapter = dummyCommentAdapter

            btnAddComment.setOnClickListener {
                findNavController().navigate(R.id.commentDialog)
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupSlider() {
        binding.apply {
            val bannerSliderAdapter = BannerSliderAdapter()
            bannerSliderAdapter.renewItems(Dummy.dummyAlbumList())
            bannerSliderAdapter.setListener(this@PropertyDetailsFragment)
            sliderAlbums.setSliderAdapter(bannerSliderAdapter)
            sliderAlbums.setCurrentPageListener {
                rvAlbum.smoothScrollToPosition(it)
                dummyAlbumAdapter.setCurrentPosition(it)
            }
        }
    }

    private fun setupAlbum() {
        binding.apply {
            dummyAlbumAdapter.setListener(this@PropertyDetailsFragment)
            dummyAlbumAdapter.submitList(Dummy.dummyAlbumList())
            rvAlbum.adapter = dummyAlbumAdapter
        }
    }

    private fun setupAdsSlider() {
        binding.apply {
            carouselSliderBetween.registerLifecycle(lifecycle)
            carouselSliderBetween.setData(Dummy.dummyCarouselList())
            carouselSliderBetween.setIndicator(dotAds)
            carouselSliderBetween.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemAdsBinding.inflate(
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
                    val currentBinding = binding as ItemAdsBinding
                    currentBinding.apply {

                    }

                }
            }
        }
    }

    private fun setupSummaryTab() {
        selectedTabPosition(0)
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.tabs_container, PropertySummaryFragment())
        fragmentTransition.commit()
    }

    private fun setupBrokerContactTab() {
        selectedTabPosition(1)
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.tabs_container, BrokerContactFragment())
        fragmentTransition.commit()
    }

    private fun setupMapTab() {
        selectedTabPosition(2)
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.tabs_container, MapFragment())
        fragmentTransition.commit()
    }

    private fun selectedTabPosition(position: Int) {
        binding.apply {
            tvSummary.setBackgroundResource(R.drawable.ic_gray_end)
            tvBroker.setBackgroundResource(R.color.gray_low)
            tvLocation.setBackgroundResource(R.drawable.ic_gray_start)

            tvSummary
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark))
            tvLocation
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark))
            tvBroker
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark))

            when (position) {
                0 -> {
                    tvSummary.setBackgroundResource(R.drawable.ic_blue_end)
                    tvSummary.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                1 -> {
                    tvBroker.setBackgroundResource(R.color.amtalek_blue_dark)
                    tvBroker.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                2 -> {
                    tvLocation.setBackgroundResource(R.drawable.ic_blue_start)
                    tvLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }
        }
    }

    override fun onImgClick(position: Int) {
        binding.apply {
            sliderAlbums.currentPagePosition = position
        }
    }

    override fun onBannerClick(position: Int) {
        findNavController().navigate(
            R.id.sliderZoomFragment,
            SliderZoomFragmentArgs(
                dummyAlbumAdapter.currentList.toTypedArray(),
                position
            ).toBundle()
        )
    }

}