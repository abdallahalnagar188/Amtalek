package eramo.amtalek.presentation.ui.drawer.myaccount

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentEditPersonalDetailsBinding
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.EditPersonalDetailsViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class EditPersonalDetailsFragment : BindingFragment<FragmentEditPersonalDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentEditPersonalDetailsBinding::inflate

    private val viewModel by viewModels<EditPersonalDetailsViewModel>()
//    private val args by navArgs<EditPersonalDetailsFragmentArgs>()
    private val viewModelShared: SharedViewModel by activityViewModels()

    //    private val memberModel get() = args.memberModel
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
//        super.registerApiRequest { viewModel.countries() }
//        super.registerApiCancellation { viewModel.cancelRequest() }

        val activityResultLauncher = registerForActivityResult(activityResultContract) {
            it?.let { uri ->
                imageUri = uri
                if (it.toString().isNotEmpty())
                    binding.ivProfile.setImageURI(it)
            }
        }

        setupViews()
        listeners()

//        fetchDateValue()
    }

    private fun listeners() {
        binding.apply {
            inToolbar.ivBack.setOnClickListener { findNavController().popBackStack() }
            btnConfirm.setOnClickListener { findNavController().popBackStack() }

//            cvCamera.setOnClickListener { activityResultLauncher.launch(null) }
        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        binding.inToolbar.tvTitle.text = getString(R.string.edit_profile)

        setupCountriesSpinner()
        setupCitiesSpinner()
    }

    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupCountriesSpinner() {
//        val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), Dummy.dummyCountriesList())
//        binding.countriesSpinner.adapter = countriesSpinnerAdapter

        binding.countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val model = parent?.getItemAtPosition(position) as CountriesSpinnerModel
//
//                Toast.makeText(requireContext(), model.countryName, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupCitiesSpinner() {
//        val citiesSpinnerAdapter = CitiesSpinnerAdapter(requireContext(), Dummy.dummyCitiesList())
//        binding.citiesSpinner.adapter = citiesSpinnerAdapter

        binding.citiesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val model = parent?.getItemAtPosition(position) as CountriesSpinnerModel
//
//                Toast.makeText(requireContext(), model.countryName, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun fetchDateValue() {
        viewModelShared.dateString.observe(viewLifecycleOwner) {
            it?.let { value ->
//                binding.tvDate.text = value
                viewModelShared.dateString.value = null
            }
        }
    }
}