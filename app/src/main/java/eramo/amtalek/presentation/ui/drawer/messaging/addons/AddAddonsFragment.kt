package eramo.amtalek.presentation.ui.drawer.messaging.addons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
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
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddonsFragment : BindingFragment<FragmentAddAdomsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddAdomsBinding::inflate

    private lateinit var rvAddonsMonthlyPriceAdapter: RvAddonsMonthlyPriceAdapter

    private lateinit var rvAddonsYearlyPriceAdapter: RvAddonsYearlyPriceAdapter

    private val viewModel: AddonsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        toggleSetup()


        rvAddonsMonthlyPriceAdapter = RvAddonsMonthlyPriceAdapter { totalPrice ->
            binding.tvTotalPriceAmount.text = getString(R.string.s_egp, totalPrice.toString())
        }
        rvAddonsYearlyPriceAdapter = RvAddonsYearlyPriceAdapter { totalPrice ->
            binding.tvTotalPriceAmount.text = getString(R.string.s_egp, totalPrice.toString())

        }

    }

    private fun setupToolBar() {
        binding.inToolbar.tvTitle.text = getString(R.string.add_adoms)
        binding.inToolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun toggleSetup() {
        binding.userToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
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
                                viewModel.addons.collect { it ->
                                    when (it) {
                                        is Resource.Success -> {
                                            viewModel.addons.value.data?.data?.let { initMonthlyRv(it) }
                                            setupListeners(it.data?.data ?: emptyList(), "monthly")
                                            LoadingDialog.dismissDialog()
                                        }

                                        is Resource.Loading -> {
                                            LoadingDialog.showDialog()
                                        }

                                        is Resource.Error -> {
                                            LoadingDialog.dismissDialog()
                                        }
                                    }
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
                                viewModel.addons.collect { it ->
                                    when (it) {
                                        is Resource.Success -> {
                                            viewModel.addons.value.data?.data?.let { initYearlyRv(it) }
                                            setupListeners(it.data?.data ?: emptyList(), "yearly")
                                            LoadingDialog.dismissDialog()
                                        }

                                        is Resource.Loading -> {
                                            LoadingDialog.showDialog()
                                        }

                                        is Resource.Error -> {
                                            LoadingDialog.dismissDialog()
                                        }
                                    }

                                }
                            }

                        }
                    }
                }
            }
        }

        binding.monthlyBtnAddon.isChecked = true
    }


    private fun initMonthlyRv(data: List<Data?>) {
        if (data.isNotEmpty()) {
            binding.recyclerViewAddons.visibility = View.VISIBLE
            binding.recyclerViewAddons.adapter = rvAddonsMonthlyPriceAdapter
            rvAddonsMonthlyPriceAdapter.submitList(data)
            rvAddonsMonthlyPriceAdapter.notifyDataSetChanged()
        } else {
            binding.recyclerViewAddons.visibility = View.GONE
        }
    }

    private fun initYearlyRv(data: List<Data?>) {
        if (data.isNotEmpty()) {
            binding.recyclerViewAddons.visibility = View.VISIBLE
            binding.recyclerViewAddons.adapter = rvAddonsYearlyPriceAdapter
            rvAddonsYearlyPriceAdapter.submitList(data)
            rvAddonsYearlyPriceAdapter.notifyDataSetChanged()

        } else {
            binding.recyclerViewAddons.visibility = View.GONE
        }
    }


    private fun setupListeners(data: List<Data?>, durations: String) {

        binding.btnTotalPrice.setOnClickListener {
            if (binding.tvTotalPriceAmount.text != getString(R.string.s_egp, "0.0")) {
                findNavController().navigate(
                    R.id.buyAddonsFragment,
                    bundleOf(
                        "addon" to ItemCard(
                            if (binding.monthlyBtnAddon.isChecked) {
                                rvAddonsMonthlyPriceAdapter.getUpdatedDataList()
                            } else {
                                rvAddonsYearlyPriceAdapter.getUpdatedDataList()
                            },
                            if (binding.monthlyBtnAddon.isChecked) {
                                rvAddonsMonthlyPriceAdapter.calculateTotalPrice(data).toInt()
                            } else {
                                rvAddonsYearlyPriceAdapter.calculateTotalPrice(data).toInt()
                            }, deuration = if (durations == "monthly") {
                                "monthly"
                            } else {
                                "yearly"
                            }
                        )
                    )
                )
            } else {
                showToast(getString(R.string.please_select_addons_to_buy))
            }
        }
    }
}
