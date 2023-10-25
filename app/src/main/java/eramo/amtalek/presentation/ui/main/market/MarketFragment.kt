package eramo.amtalek.presentation.ui.main.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMarketBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvMarketAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class MarketFragment : BindingFragment<FragmentMarketBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMarketBinding::inflate

    @Inject
    lateinit var rvMarketAdapter: RvMarketAdapter

    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        initRv()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            tvTitle.text = getString(R.string.s_market, Dummy.dummyMarketPostsList().size.toString())
        }
    }

    private fun initRv() {
        binding.rv.adapter = rvMarketAdapter
        rvMarketAdapter.submitList(Dummy.dummyMarketPostsList())
    }
}