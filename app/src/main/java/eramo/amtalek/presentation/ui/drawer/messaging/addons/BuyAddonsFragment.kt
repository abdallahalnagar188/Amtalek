package eramo.amtalek.presentation.ui.drawer.messaging.addons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
    }

    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.payment)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    private fun setupViews(){
        binding.apply {
            tvItem1Label.text = getString(R.string.normal_listings)
            tvItem2Label.text = getString(R.string.featured_listings)
            tvItem3Label.text = getString(R.string.projects)
            tvItem4Label.text = getString(R.string.messages)
            tvItem1Value.text = itemCard.card.get(0).monthlyPrice
            tvItem2Value.text = itemCard.card.get(1).monthlyPrice
            tvItem3Value.text = itemCard.card.get(2).monthlyPrice
            tvItem4Value.text = itemCard.card.get(3).monthlyPrice

            tvItem1Counter.text = itemCard.card.get(0).quantity.toString()
            tvItem2Counter.text = itemCard.card.get(1).quantity.toString()
            tvItem3Counter.text = itemCard.card.get(2).quantity.toString()
            tvItem4Counter.text = itemCard.card.get(3).quantity.toString()

        }
    }
}