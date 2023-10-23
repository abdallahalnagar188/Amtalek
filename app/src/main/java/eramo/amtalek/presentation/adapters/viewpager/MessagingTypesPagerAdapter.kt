package eramo.amtalek.presentation.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingChatFragment
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingOfferFragment


class MessagingTypesPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> MessagingOfferFragment()
            else -> MessagingChatFragment()
        }
    }
}