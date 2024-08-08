package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddAdomsBinding
import eramo.amtalek.presentation.ui.BindingFragment

class AddAdomsFragment : BindingFragment<FragmentAddAdomsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddAdomsBinding::inflate

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
                    R.id.monthly_btn -> {
                        binding.apply {
                            monthlyBtnAdom.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            monthlyBtnAdom.setTextColor(context?.getColor(R.color.white)!!)
                            yearlyBtnAdom.setBackgroundColor(context?.getColor(R.color.white)!!)
                            yearlyBtnAdom.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            yearlyBtnAdom.setBackgroundColor(context?.getColor(R.color.white)!!)
//                            usersYearlyRv.visibility = View.GONE
//                            usersMonthlyRv.visibility = View.VISIBLE
                        }
                    }

                    R.id.yearly_btn -> {
                        binding.apply {
                            yearlyBtnAdom.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            yearlyBtnAdom.setTextColor(context?.getColor(R.color.white)!!)
                            monthlyBtnAdom.setBackgroundColor(context?.getColor(R.color.white)!!)
                            monthlyBtnAdom.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
//                            usersMonthlyRv.visibility = View.GONE
//                            usersYearlyRv.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.monthlyBtnAdom.isChecked = true
//
//        binding.agencyToggleGroup.addOnButtonCheckedListener{ toggleButtonGroup, checkedId, isChecked ->
//
//            if (isChecked){
//                when(checkedId){
//                    R.id.monthly_agency_btn -> {
//                        binding.apply {
//                            monthlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
//                            monthlyAgencyBtn.setTextColor(context?.getColor(R.color.white)!!)
//                            yearlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
//                            yearlyAgencyBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
//                            agenciesYearlyRv.visibility = View.GONE
//                            agenciesMonthlyRv.visibility = View.VISIBLE
//                        }
//                    }
//                    R.id.yearly_agency_btn -> {
//                        binding.apply {
//                            yearlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
//                            yearlyAgencyBtn.setTextColor(context?.getColor(R.color.white)!!)
//                            monthlyAgencyBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
//                            monthlyAgencyBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
//                            agenciesMonthlyRv.visibility = View.GONE
//                            agenciesYearlyRv.visibility = View.VISIBLE
//                        }
//                    }
//                }
//            }

//
//        }
//        binding.monthlyAgencyBtn.isChecked = true

    }
}