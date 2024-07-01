package eramo.amtalek.presentation.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import eramo.amtalek.presentation.ui.main.offers.HotOffersForBothFragment
import eramo.amtalek.presentation.ui.main.offers.HotOffersRentFragment
import eramo.amtalek.presentation.ui.main.offers.HotOffersSellFragment


class HotOffersTypesPagerAdapter(list:ArrayList<Fragment>,fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var fragmentList = list
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HotOffersForBothFragment()
            1 -> HotOffersSellFragment()
            else -> {HotOffersRentFragment()}
        }
    }
}