package eramo.amtalek.presentation.ui.drawer.messaging.addons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBuyAddonsBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class BuyAddonsFragment : BindingFragment<FragmentBuyAddonsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBuyAddonsBinding::inflate

    private val viewModel: AddonsViewModel by viewModels()

    private lateinit var itemCard: ItemCard


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: BuyAddonsFragmentArgs by navArgs()
        itemCard = args.addon

        arguments?.let {
            itemCard = it.getSerializable("addon") as ItemCard
        }
        setupToolBar()
        setupViews()
        setupToggle()
    }

    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.payment)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    private fun setupToggle() {
        binding.userToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.cash_btn -> {
                        binding.apply {
                            cashBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            cashBtn.setTextColor(context?.getColor(R.color.white)!!)

                            visaBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            visaBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                        }
                    }

                    R.id.visa_btn -> {
                        binding.apply {
                            visaBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            visaBtn.setTextColor(context?.getColor(R.color.white)!!)

                            cashBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            cashBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                        }
                    }
                }
            }
        }

        binding.cashBtn.isChecked = true
    }

    fun fetchData() {
        viewModel.buyAddons(
           itemCard
        )
    }
    private fun setupViews(){
        binding.apply {
            tvItem1Label.text = getString(R.string.normal_listings)
            tvItem2Label.text = getString(R.string.featured_listings)
            tvItem3Label.text = getString(R.string.projects)
            tvItem4Label.text = getString(R.string.messages)
            if (itemCard.deuration == "monthly"){
                tvItem1Value.text = getString(R.string.s_egp, itemCard.card[0].monthlyPrice)
                tvItem2Value.text = getString(R.string.s_egp, itemCard.card[1].monthlyPrice)
                tvItem3Value.text = getString(R.string.s_egp, itemCard.card[2].monthlyPrice)
                tvItem4Value.text = getString(R.string.s_egp, itemCard.card[3].monthlyPrice)
            }else{
                tvItem1Value.text = getString(R.string.s_egp, itemCard.card[0].yearlyPrice)
                tvItem2Value.text = getString(R.string.s_egp, itemCard.card[1].yearlyPrice)
                tvItem3Value.text = getString(R.string.s_egp, itemCard.card[2].yearlyPrice)
                tvItem4Value.text = getString(R.string.s_egp, itemCard.card[3].yearlyPrice)
            }
            tvItem1Counter.text = itemCard.card[0].quantity.toString()
            tvItem2Counter.text = itemCard.card[1].quantity.toString()
            tvItem3Counter.text = itemCard.card[2].quantity.toString()
            tvItem4Counter.text = itemCard.card[3].quantity.toString()

            tvDurationValue.text =
                if (itemCard.deuration == "monthly") getString(R.string.monthly) else getString(R.string.yearly)
            tvTotalPriceValue.text = getString(R.string.s_egp, itemCard.totalPrice.toString())

            btnBuy.setOnClickListener {
                fetchData()
            }
        }
    }
}