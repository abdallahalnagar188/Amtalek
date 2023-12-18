package eramo.amtalek.presentation.ui.drawer

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentJoinUsBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.showToast

@AndroidEntryPoint
class JoinUsFragment : BindingFragment<FragmentJoinUsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentJoinUsBinding::inflate


//    @Inject
//    lateinit var rvLatestProjectsAdapter: RvLatestProjectsAdapter
//    private val viewModelShared: SharedViewModel by activityViewModels()

    private lateinit var imagePickedUri: Uri

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    imagePickedUri = fileUri
                    binding.ivImagePicked.setImageURI(fileUri)
                    handelImagePickerViewsVisibility()
                }

                ImagePicker.RESULT_ERROR -> {
                    showToast(ImagePicker.getError(data))
                }

                else -> {
                    showToast(getString(R.string.no_image_chosen))
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        setupToolbar()
    }

    private fun listeners() {
        binding.apply {
            identityCard.setOnClickListener { pickImage() }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.join_us)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun pickImage() {
        ImagePicker.with(requireActivity())
            .compress(1024)
            .cropSquare()
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun handelImagePickerViewsVisibility() {
        binding.apply {
            ivImagePicked.visibility = View.VISIBLE

            ivPickImage.visibility = View.GONE
            tvUploadId.visibility = View.GONE
            tvIdFormat.visibility = View.GONE
        }
    }
}