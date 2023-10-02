package eramo.amtalek.presentation.ui.drawer.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentChangePasswordBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.ChangePasswordViewModel
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class ChangePasswordFragment : BindingFragment<FragmentChangePasswordBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChangePasswordBinding::inflate

    private val viewModel by viewModels<ChangePasswordViewModel>()
    private val args by navArgs<ChangePasswordFragmentArgs>()
//    private val memberModel get() = args.memberModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiCancellation { viewModel.cancelRequest() }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue)

        binding.apply {
            btnSave.setOnClickListener { findNavController().popBackStack() }
            ivBack.setOnClickListener { findNavController().popBackStack() }

//            Glide.with(requireContext())
//                .load(EventsApi.IMAGE_URL_PROFILE + memberModel.mImage)
//                .into(FChangePasswordProfile)
        }

    }

}