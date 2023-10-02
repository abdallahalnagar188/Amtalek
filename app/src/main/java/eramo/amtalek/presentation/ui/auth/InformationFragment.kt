package eramo.amtalek.presentation.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentInformationBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.auth.PolicyViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.deeplink.DeeplinkUtil
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class InformationFragment : BindingFragment<FragmentInformationBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInformationBinding::inflate

    private val viewModel by viewModels<PolicyViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()

    private var imageUri: Uri? = null
    private val activityResultContract = object : ActivityResultContract<Any, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .getIntent(requireActivity())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (resultCode == AppCompatActivity.RESULT_OK)
                return CropImage.getActivityResult(intent).uri
            return Uri.parse("")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.gray_low)

        val activityResultLauncher = registerForActivityResult(activityResultContract) {
            it?.let { uri ->
                imageUri = uri
                if (it.toString().isNotEmpty())
                    binding.ivProfile.setImageURI(it)
            }
        }

        binding.apply {

            cvBack.setOnClickListener { findNavController().popBackStack() }
            cvCamera.setOnClickListener {
                activityResultLauncher.launch(null)
            }
            itlDate.setOnClickListener {
                findNavController().navigate(
                    NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toDatePicker()).build()
                )
            }
            signupBtnSignup.setOnClickListener {
                findNavController().navigate(
                    R.id.nav_main, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_auth, true)
                        .setPopUpTo(R.id.nav_main, true)
                        .build()
                )
            }
        }
        fetchDateValue()
    }

    private fun fetchDateValue() {
        viewModelShared.dateString.observe(viewLifecycleOwner) {
            it?.let { value ->
                binding.tvDate.text = value
                viewModelShared.dateString.value = null
            }
        }
    }
}