package eramo.amtalek.presentation.ui.main.market

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
import eramo.amtalek.databinding.FragmentMarketBinding
import eramo.amtalek.domain.model.main.market.MarketPostsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMarketAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.extension.imagviewer.ImagesListFragmentArgs
import eramo.amtalek.presentation.ui.social.CommentsBottomDialogFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.HomeViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarketFragment : BindingFragment<FragmentMarketBinding>(),
    RvMarketAdapter.OnItemClickListener,
    CommentsBottomDialogFragment.CommentsBottomDialogOnClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMarketBinding::inflate

    @Inject
    lateinit var rvMarketAdapter: RvMarketAdapter
//    private val viewModel by viewModels<HomeViewModel>()

    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        fetchData()

    }

    private fun fetchData() {
    }


    private fun setupViews() {
        setupToolbar()
        initRv()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
//            tvTitle.text =
//                getString(R.string.s_market, Dummy.dummyMarketPostsList().size.toString())
//                getString(R.string.social_network)
        }
    }

    private fun initRv() {
        rvMarketAdapter.setListener(this@MarketFragment)
        binding.rv.adapter = rvMarketAdapter
        rvMarketAdapter.submitList(Dummy.dummyMarketPostsList())
    }

    private fun initBottomSheetDialog() {
//        binding.inToolbar.tvTitle.setOnClickListener {

        CommentsBottomDialogFragment().setListener(this@MarketFragment)
        CommentsBottomDialogFragment().show(this@MarketFragment.childFragmentManager, "")
//        }
    }

    override fun onBottomSheetDialogClick(text: String) {
        showToast(text)
    }


    //------------------------------------- AdvertisementViewHolder ------------------------------------- //
    override fun onCommentsClickAdvertisementPost(model: MarketPostsModel) {
        initBottomSheetDialog()
    }

    // ------------------------------------- TextViewHolder ------------------------------------- //
    override fun onCommentsClickTextPost(model: MarketPostsModel) {
        initBottomSheetDialog()
    }

    // ------------------------------------- TextAndPhotosViewHolderI ------------------------------------- //
    override fun onCommentsClickTextAndPhotosPost(model: MarketPostsModel) {
        initBottomSheetDialog()
    }

    // ------------------------------------- PhotosViewHolder ------------------------------------- //
    override fun onCommentsClickPhotosPost(model: MarketPostsModel) {
        initBottomSheetDialog()
    }

    override fun onPhotosClickPhotosPost(model: MarketPostsModel) {
        findNavController().navigate(
            R.id.imagesListFragment,
            ImagesListFragmentArgs(model.photosList?.toTypedArray() ?: emptyArray()).toBundle(),
            navOptionsAnimation()
        )
    }
    override fun onResume() {
        super.onResume()

    }

}