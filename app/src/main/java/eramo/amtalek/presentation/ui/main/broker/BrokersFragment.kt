package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBrokersBinding
import eramo.amtalek.domain.model.main.brokers.BrokerModel
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokersAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.CartViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class BrokersFragment : BindingFragment<FragmentBrokersBinding>(),
    RvBrokersAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var rvBrokersAdapter: RvBrokersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        initRv(Dummy.dummyBrokersList())
    }

    private fun initRv(data: List<BrokerModel>) {
        rvBrokersAdapter.setListener(this@BrokersFragment)
        binding.rvBrokers.adapter = rvBrokersAdapter
        rvBrokersAdapter.submitList(data)

        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<BrokerModel>) {
        binding.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvBrokersAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it.title.lowercase().contains(text.toString().lowercase())
                }
                rvBrokersAdapter.submitList(null)
                rvBrokersAdapter.submitList(list)
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.brokers)
            toolbarIvMenu.setOnClickListener {
                viewModelShared.openDrawer.value = true
            }
        }
    }

    override fun onBrokerClick(model: BrokerModel) {

    }
}