package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBrokersBinding
import eramo.amtalek.domain.model.main.brokers.BrokerModel
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokersAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class BrokersFragment : BindingFragment<FragmentBrokersBinding>(),
    RvBrokersAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()

    @Inject
    lateinit var rvBrokersAdapter: RvBrokersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

    }

    private fun setupViews() {
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
//            tvTitle.text = getString(R.string.brokers)
            toolbarIvMenu.setOnClickListener {
                viewModelShared.openDrawer.value = true
            }
        }

        if (LocalUtil.isEnglish()){
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))

        }else{
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }

    }

    override fun onBrokerClick(model: BrokerModel) {
//        findNavController().navigate(R.id.brokersDetailsFragment, null, navOptionsAnimation())
    }
}