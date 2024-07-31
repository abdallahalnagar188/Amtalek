package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.broker.entity.DataX
import eramo.amtalek.databinding.FragmentBrokersBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokersAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.navOptionsAnimation
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BrokersFragment : BindingFragment<FragmentBrokersBinding>(),
    RvBrokersAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: BrokersViewModel by viewModels()

    @Inject
    lateinit var rvBrokersAdapter: RvBrokersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBrokers()
//        lifecycleScope.launch {
//            viewModel.brokers.collect {
//                Log.e("elnagar", it?.data?.original?.data.toString())
//                rvBrokersAdapter.submitList(it?.data?.original?.data)
//                binding.rvBrokers.adapter = rvBrokersAdapter
//            }
//        }
        setupViews()

    }

    private fun setupViews() {
        setupToolbar()
        initRv(viewModel.brokers.value?.data?.original?.data ?: emptyList())
    }

    private fun initRv(data: List<DataX>) {
        rvBrokersAdapter.setListener(this@BrokersFragment)
        binding.rvBrokers.adapter = rvBrokersAdapter
        rvBrokersAdapter.submitList(data)

        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<DataX>) {
        binding.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvBrokersAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it.name.lowercase().contains(text.toString().lowercase())
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
            FHomeEtSearch.setOnClickListener {

            }
        }

        if (LocalUtil.isEnglish()){
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))

        }else{
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }

    }

    override fun onBrokerClick(model: DataX) {
        findNavController().navigate(
            R.id.action_brokersFragment_to_brokersDetailsFragment,
            bundleOf("id" to model.id),
            navOptionsAnimation()
        )
    }
}