package eramo.amtalek.presentation.ui.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNewsBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyNewsPreviewAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.ShopViewModel
import eramo.amtalek.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BindingFragment<FragmentNewsBinding>(),
    DummyNewsPreviewAdapter.OnItemClickListener{

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()

    @Inject
    lateinit var dummyNewsPreviewAdapter: DummyNewsPreviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StatusBarUtil.whiteWithBackground(requireActivity(),R.color.amtalek_blue_dark)
        setupToolbar()

        dummyNewsPreviewAdapter.setListener(this)
        binding.apply {
            dummyNewsPreviewAdapter.submitList(Dummy.list())
            rvNews.adapter=dummyNewsPreviewAdapter
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
//            ivSearch.setOnClickListener { findNavController().navigate(R.id.searchPropertyFragment)}
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
    }

    override fun onProductClick(model: String) {
        findNavController().navigate(R.id.newsDetailsFragment)
    }

}