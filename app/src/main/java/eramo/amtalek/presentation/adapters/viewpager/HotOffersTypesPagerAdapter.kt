package eramo.amtalek.presentation.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import eramo.amtalek.presentation.ui.main.myestate.MyEstateRentFragment
import eramo.amtalek.presentation.ui.main.myestate.MyEstateSellFragment
import eramo.amtalek.presentation.ui.main.offers.HotOffersRentFragment
import eramo.amtalek.presentation.ui.main.offers.HotOffersSellFragment


class HotOffersTypesPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> HotOffersRentFragment()
            else -> HotOffersSellFragment()
        }
    }
}