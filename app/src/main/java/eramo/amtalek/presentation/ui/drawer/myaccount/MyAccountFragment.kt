package eramo.amtalek.presentation.ui.drawer.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.databinding.FragmentMyAccountBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.MyAccountViewModel
import eramo.amtalek.util.NavKeys
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class MyAccountFragment : BindingFragment<FragmentMyAccountBinding>(), View.OnClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyAccountBinding::inflate

    private val viewModel by viewModels<MyAccountViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()
    private var memberModel: Member? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiRequest { viewModel.getProfile() }
        super.registerApiCancellation { viewModel.cancelRequest() }
        StatusBarUtil.transparent()

        binding.apply {
            FMyAccountTvEdit.setOnClickListener(this@MyAccountFragment)
            FMyAccountTvChangePassword.setOnClickListener(this@MyAccountFragment)
            FMyAccountTvSuspendAccount.setOnClickListener(this@MyAccountFragment)
            ivClose.setOnClickListener(this@MyAccountFragment)
        }
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
                    R.id.editPersonalDetailsFragment, null,
                    navOptionsAnimation()
                )
            }

            R.id.FMyAccount_tv_changePassword -> {
//                if (memberModel == null) {
//                    return
//                }
//                val args = Bundle()
//                args.putParcelable(NavKeys.MEMBER_MODEL, memberModel)
                findNavController().navigate(
                    R.id.changePasswordFragment,
                    null,
                    navOptionsAnimation()
                )
            }

            R.id.FMyAccount_tv_suspendAccount -> {
                Navigation.findNavController(requireActivity(), R.id.main_navHost)
                    .navigate(R.id.suspendDialog)
            }

            R.id.iv_close -> findNavController().popBackStack()
        }
    }
    
}