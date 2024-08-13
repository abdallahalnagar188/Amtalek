package eramo.amtalek.presentation.ui.drawer.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.databinding.FragmentMyAccountBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.MyAccountViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil

@AndroidEntryPoint
class MyAccountFragment : BindingFragment<FragmentMyAccountBinding>(), View.OnClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyAccountBinding::inflate

    private val viewModel by viewModels<MyAccountViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()
    private var memberModel: Member? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiRequest { viewModel.getProfile() }
//        super.registerApiCancellation { viewModel.cancelRequest() }
//        StatusBarUtil.blackWithBackground(requireActivity(), R.color.amtalek_blue)
        StatusBarUtil.transparent()

        binding.apply {
            FMyAccountTvEdit.setOnClickListener(this@MyAccountFragment)
            FMyAccountTvMySubscriptions.setOnClickListener(this@MyAccountFragment)
            FMyAccountTvSuspendAccount.setOnClickListener(this@MyAccountFragment)
            ivClose.setOnClickListener(this@MyAccountFragment)
            FMyAccountTvAddAddons.setOnClickListener(this@MyAccountFragment)
            FMyAccountTvChangePassword.setOnClickListener(this@MyAccountFragment)

            Glide.with(requireContext()).load(
                if (UserUtil.getUserProfileImageUrl() != "") {
                    UserUtil.getUserProfileImageUrl()
                } else {
                    R.drawable.avatar
                }
            ).into(ivProfile)

            textView20.text = getString(R.string.S_user_name, UserUtil.getUserFirstName(), UserUtil.getUserLastName())
        }
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.FMyAccount_tv_edit -> {
//                if (memberModel == null) {
//                    return
//                }
//                val args = Bundle()
//                args.putParcelable(NavKeys.MEMBER_MODEL, memberModel)
                findNavController().navigate(
                    R.id.editPersonalDetailsFragment
                )
            }

            R.id.FMyAccount_tv_my_subscriptions -> {
//                if (memberModel == null) {
//                    return
//                }
//                val args = Bundle()
//                args.putParcelable(NavKeys.MEMBER_MODEL, memberModel)
                findNavController().navigate(
                    R.id.packageDetailsFragment
                )
            }

            R.id.FMyAccount_tv_change_password -> {
                findNavController().navigate(
                    R.id.changePasswordFragment
                )
            }


            R.id.FMyAccount_tv_suspendAccount -> {
                Navigation.findNavController(requireActivity(), R.id.main_navHost)
                    .navigate(R.id.suspendDialog)
            }

            R.id.FMyAccount_tv_add_addons -> {
                findNavController().navigate(
                    R.id.addAdomsFragment
                )
            }

            R.id.iv_close -> findNavController().popBackStack()
        }
    }

}