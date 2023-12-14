package eramo.amtalek.presentation.ui.drawer.messaging.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUsersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class UsersChatFragment : BindingFragment<FragmentUsersChatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUsersChatBinding::inflate

    @Inject
    lateinit var rvUsersChatAdapter: RvUsersChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        setupToolbar()

        assignFakeData()
        initRvChat()
    }

    private fun setupListeners() {
        binding.apply {
            viewUserHeader.setOnClickListener {
                findNavController().navigate(R.id.userProfileFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            tvTitle.text = getString(R.string.chat_messaging)
        }
    }

    private fun assignFakeData() {
        binding.apply {
            tvUserName.text = "Erlan Sadewa"
            tvUserId.text = "erlan.sadewa"
            Glide.with(requireContext())
                .load("https://preview.keenthemes.com/metronic-v4/theme/assets/pages/media/profile/profile_user.jpg").into(ivUserImage)


        }
    }

    private fun initRvChat() {
        binding.rv.adapter = rvUsersChatAdapter
        rvUsersChatAdapter.submitList(Dummy.dummyChatList())

    }
}