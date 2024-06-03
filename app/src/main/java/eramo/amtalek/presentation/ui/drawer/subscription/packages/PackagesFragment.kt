package eramo.amtalek.presentation.ui.drawer.subscription.packages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPackagesBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.presentation.adapters.recyclerview.RvPackagesAgencyMonthlyAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvPackagesAgencyYearlyAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvPackagesUserMonthlyAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvPackagesUserYearlyAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PackagesFragment : BindingFragment<FragmentPackagesBinding>(),RvPackagesUserMonthlyAdapter.UserMonthlyClickListener,RvPackagesUserYearlyAdapter.UserYearlyClickListener,
RvPackagesAgencyYearlyAdapter.AgencyYearlyClickListener,RvPackagesAgencyMonthlyAdapter.AgencyMonthlyClickListener{
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPackagesBinding::inflate
   private val viewModel by viewModels<PackagesViewModel>()

    @Inject
    lateinit var rvUserPackagesMonthlyAdapter: RvPackagesUserMonthlyAdapter

    @Inject
    lateinit var rvUserPackagesYearlyAdapter: RvPackagesUserYearlyAdapter
    ////////////////////////////----------------------------------------////////////////////////////
    @Inject
    lateinit var rvAgencyPackagesMonthlyAdapter: RvPackagesAgencyMonthlyAdapter

    @Inject
    lateinit var rvAgencyPackagesYearlyAdapter: RvPackagesAgencyYearlyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        makeRequests()
        fetchData()
    }

    private fun fetchData() {
        fetchUserPackages()
        fetchAgencyPackages()
        fetchUserAndAgencyPackages()
        fetchSubscribeToPackage()
    }

    private fun fetchSubscribeToPackage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subscribeToPackagesState.collect(){state->
                    when (state){
                        is UiState.Success->{
                            LoadingDialog.dismissDialog()
                            Toast.makeText(requireContext(), state.data?.message, Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Error->{
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun fetchUserAndAgencyPackages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.initScreenState.collect(){state->
                    when (state){
                        is UiState.Success->{

                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{

                            LoadingDialog.showDialog()
                        }
                        is UiState.Error->{
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }

    }

    private fun fetchAgencyPackages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAgencyPackageState.collect(){state->
                    when (state){
                        is UiState.Success->{
                            val data = state.data
                            initAgencyYearlyRv(data)
                            initAgencyMonthlyRv(data)
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{

                            LoadingDialog.showDialog()
                        }
                        is UiState.Error->{
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun fetchUserPackages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserPackageState.collect(){state->
                    when (state){
                        is UiState.Success->{
                            LoadingDialog.dismissDialog()

                            val data = state.data
                            initUserYearlyRv(data)
                            initUserMonthlyRv(data)
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Error->{
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun makeRequests() {
        if (!UserUtil.isUserLogin()){
            viewModel.getUserAndAgencyPackages()
        }
        if (UserUtil.getUserType()=="user"){
            viewModel.getUserPackages()
            binding.placeHolder.visibility = View.GONE
            binding.agencyToggleGroup.visibility = View.GONE
            binding.agencyTv.visibility = View.GONE
            binding.agenciesMonthlyRv.visibility = View.GONE
            binding.agenciesYearlyRv.visibility = View.GONE

        }
        if (UserUtil.getUserType()=="broker"){
            viewModel.getAgencyPackages()
            binding.placeHolder.visibility = View.GONE
            binding.userToggleGroup.visibility = View.GONE
            binding.userTv.visibility = View.GONE
            binding.usersMonthlyRv.visibility = View.GONE
            binding.usersYearlyRv.visibility = View.GONE
        }
    }

    private fun setupViews() {
        setupToolbar()
        toggleSetup()
    }

    private fun toggleSetup() {
        binding.userToggleGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
                     R.id.monthly_btn -> {
                        binding.apply {
                            monthlyBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            monthlyBtn.setTextColor(context?.getColor(R.color.white)!!)
                            yearlyBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            yearlyBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            usersYearlyRv.visibility = View.GONE
                            usersMonthlyRv.visibility = View.VISIBLE
                        }
                    }
                    R.id.yearly_btn -> {
                        binding.apply {
                            yearlyBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            yearlyBtn.setTextColor(context?.getColor(R.color.white)!!)
                            monthlyBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            monthlyBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            usersMonthlyRv.visibility = View.GONE
                            usersYearlyRv.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.monthlyBtn.isChecked = true

        binding.agencyToggleGroup.addOnButtonCheckedListener{ toggleButtonGroup, checkedId, isChecked ->

            if (isChecked){
                when(checkedId){
                    R.id.monthly_agency_btn -> {
                        binding.apply {
                            monthlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            monthlyAgencyBtn.setTextColor(context?.getColor(R.color.white)!!)
                            yearlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            yearlyAgencyBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            agenciesYearlyRv.visibility = View.GONE
                            agenciesMonthlyRv.visibility = View.VISIBLE
                        }
                    }
                    R.id.yearly_agency_btn -> {
                        binding.apply {
                            yearlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            yearlyAgencyBtn.setTextColor(context?.getColor(R.color.white)!!)
                            monthlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            monthlyAgencyBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            agenciesMonthlyRv.visibility = View.GONE
                            agenciesYearlyRv.visibility = View.VISIBLE
                        }
                    }
                }
            }


        }
        binding.monthlyAgencyBtn.isChecked = true

    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.packages)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }
    private fun initAgencyMonthlyRv(data: List<PackageModel?>?) {
        binding.agenciesMonthlyRv.adapter = rvAgencyPackagesMonthlyAdapter
        rvAgencyPackagesMonthlyAdapter.submitList(data)
        rvAgencyPackagesMonthlyAdapter.setListener(this)
    }
    private fun initAgencyYearlyRv(data: List<PackageModel?>?) {
        binding.agenciesYearlyRv.adapter = rvAgencyPackagesYearlyAdapter
        rvAgencyPackagesYearlyAdapter.submitList(data)
        rvAgencyPackagesYearlyAdapter.setListener(this)

    }
    private fun initUserMonthlyRv(data: List<PackageModel?>?) {
        binding.usersMonthlyRv.adapter = rvUserPackagesMonthlyAdapter
        rvUserPackagesMonthlyAdapter.submitList(data)
        rvUserPackagesMonthlyAdapter.setListener(this)

    }
    private fun initUserYearlyRv(data: List<PackageModel?>?) {
        binding.usersYearlyRv.adapter = rvUserPackagesYearlyAdapter
        rvUserPackagesYearlyAdapter.submitList(data)
        rvUserPackagesYearlyAdapter.setListener(this)

    }

    override fun onAgencyMonthlyClick(model: PackageModel) {
        viewModel.subscribeToPackage(duration = "monthly", packageId = model.id.toString(), actorType = "broker")
    }

    override fun onAgencyYearlyPlanClick(model: PackageModel) {
        viewModel.subscribeToPackage(duration = "yearly", packageId = model.id.toString(), actorType = "broker")
    }

    override fun onUserMonthlyClick(model: PackageModel) {
        viewModel.subscribeToPackage(duration = "monthly", packageId = model.id.toString(), actorType = "user")
    }

    override fun onUserYearlyClick(model: PackageModel) {
        viewModel.subscribeToPackage(duration = "yearly", packageId = model.id.toString(), actorType = "user")
    }
}

