package eramo.amtalek.presentation.ui.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.databinding.ItemNewsBinding
import eramo.amtalek.presentation.adapters.dummy.DummyFeaturedAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.CartViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment : BindingFragment<FragmentNewsDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var dummyFeaturedAdapter: DummyFeaturedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiRequest { viewModel.cartData() }
        super.registerApiCancellation { viewModel.cancelRequest() }

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        setupCarouselNews()
        binding.apply {
            dummyFeaturedAdapter.submitList(Dummy.list())
            rvProperties.adapter = dummyFeaturedAdapter
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupCarouselNews() {
        binding.apply {
            carouselNews.registerLifecycle(lifecycle)
            carouselNews.setData(Dummy.dummyCarouselList())
            carouselNews.setIndicator(dotShowcase)
            carouselNews.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemNewsBinding.inflate(
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
                    val currentBinding = binding as ItemNewsBinding
                    currentBinding.apply {

                    }
                }
            }
        }
    }

}