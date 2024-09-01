package eramo.amtalek.presentation.ui.drawer.invoices

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.Favorite
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.HistoryPackagesInfo
import eramo.amtalek.databinding.DialogInvoiceDetailsBinding
import eramo.amtalek.databinding.FragmentInvoiceBinding
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.databinding.FragmentMyInvoicesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.social.MyProfileViewModel
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import javax.inject.Inject

@AndroidEntryPoint
class InvoiceFragment : BindingFragment<FragmentInvoiceBinding>(),
    RvInvoicesAdapter.InvoicesClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInvoiceBinding::inflate

    private val viewModel: MyProfileViewModel by viewModels()

    @Inject
    lateinit var rvInvoiceAdapter: RvInvoicesAdapter

    @Inject
    lateinit var rvTypeInInvoiceDetailsAdapter: RvTypeInInvoiceDetailsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
        setupViews()
        fetchGetProfileState()
    }


    private fun setupViews() {
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.my_invoices)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }


    private fun initRvMyFavList(invoiceList: List<HistoryPackagesInfo>) {

        binding.rvInvoices.adapter = rvInvoiceAdapter
        rvInvoiceAdapter.submitList(invoiceList)
        rvInvoiceAdapter.setListener(this@InvoiceFragment)
    }

    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            state.data?.historyPackages?.let { initRvMyFavList(it) }
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun showInvoiceDetailsDialog(model: HistoryPackagesInfo) {
        val dialogBinding = DialogInvoiceDetailsBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = AlertDialog.Builder(requireContext(), R.style.DialogStyle)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.apply {
            tvTitle.text = getString(R.string.s_invoices, model.expirationDate?.packageId.toString())
            tvStartDate.text = model.expirationDate?.expirationDate
            tvEnd.text = model.expirationDate?.dateOfPackage
            rv.adapter = rvTypeInInvoiceDetailsAdapter
            rvTypeInInvoiceDetailsAdapter.submitList(model.packageDetails)
            tvAcceptanceValue.text = model.expirationDate?.status
            tvTotalPriceValue.text = getString(R.string.s_egp, model.actualPayment)

            when (model.expirationDate?.status) {
                "accepted" -> tvAcceptanceValue.text =getString(R.string.accepted)
                "holding" -> tvAcceptanceValue.text = getString(R.string.holding)
            }

            btnOk.setOnClickListener { dialog.dismiss() }
        }

        dialog.show()

    }

    override fun onInvoicesClick(model: HistoryPackagesInfo) {
        showInvoiceDetailsDialog(model)

    }

}