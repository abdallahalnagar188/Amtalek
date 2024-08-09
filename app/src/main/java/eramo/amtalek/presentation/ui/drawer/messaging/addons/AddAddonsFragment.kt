package eramo.amtalek.presentation.ui.drawer.messaging.addons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.adons.Data
import eramo.amtalek.databinding.FragmentAddAdomsBinding
import eramo.amtalek.presentation.adapters.recyclerview.messaging.addons.RvAddonsMonthlyPriceAdapter
import eramo.amtalek.presentation.adapters.recyclerview.messaging.addons.RvAddonsYearlyPriceAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddonsFragment : BindingFragment<FragmentAddAdomsBinding>(),
    RvAddonsMonthlyPriceAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddAdomsBinding::inflate

    private val rvAddonsMonthlyPriceAdapter = RvAddonsMonthlyPriceAdapter()

    private val rvAddonsYearlyPriceAdapter = RvAddonsYearlyPriceAdapter()

    private val viewModel: AddonsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        toggleSetup()


    }

    private fun setupToolBar() {
        binding.inToolbar.tvTitle.text = getString(R.string.add_adoms)
        binding.inToolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun toggleSetup() {
        binding.userToggleGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.monthly_btn_addon -> {
                        binding.apply {
                            // Update Monthly Button Styles
                            monthlyBtnAddon.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            monthlyBtnAddon.setTextColor(context?.getColor(R.color.white)!!)

                            // Update Yearly Button Styles
                            yearlyBtnAddon.setBackgroundColor(context?.getColor(R.color.white)!!)
                            yearlyBtnAddon.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)

                            viewModel.getAddons()
                            lifecycleScope.launch {
                                viewModel.addons.collect {
                                    viewModel.addons.value.data?.data?.let { initMonthlyRv(it) }
                                }
                            }
                        }
                    }

                    R.id.yearly_btn_addon -> {
                        binding.apply {
                            // Update Yearly Button Styles
                            yearlyBtnAddon.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            yearlyBtnAddon.setTextColor(context?.getColor(R.color.white)!!)

                            // Update Monthly Button Styles
                            monthlyBtnAddon.setBackgroundColor(context?.getColor(R.color.white)!!)
                            monthlyBtnAddon.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)

                            viewModel.getAddons()
                            lifecycleScope.launch {
                                viewModel.addons.collect {
                                    viewModel.addons.value.data?.data?.let { initYearlyRv(it) }

                                }
                            }
                        }
                    }
                }
            }
        }

        // Set default selection if necessary
        binding.monthlyBtnAddon.isChecked = true
    }


    private fun initMonthlyRv(data: List<Data?>) {
        if (data.isNotEmpty()) {
            binding.recyclerViewAddons.visibility = View.VISIBLE
            binding.recyclerViewAddons.adapter = rvAddonsMonthlyPriceAdapter
            rvAddonsMonthlyPriceAdapter.submitList(data)
        } else {
            binding.recyclerViewAddons.visibility = View.GONE
        }
    }

    private fun initYearlyRv(data: List<Data?>) {
        if (data.isNotEmpty()) {
            binding.recyclerViewAddons.visibility = View.VISIBLE
            binding.recyclerViewAddons.adapter = rvAddonsYearlyPriceAdapter
            rvAddonsYearlyPriceAdapter.submitList(data)
        } else {
            binding.recyclerViewAddons.visibility = View.GONE
        }
    }

    override fun onPriceUpdate(model: Data, totalPrice: Int) {
        
    }
}
