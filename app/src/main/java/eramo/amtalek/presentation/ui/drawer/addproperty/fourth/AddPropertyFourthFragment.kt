package eramo.amtalek.presentation.ui.drawer.addproperty.fourth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyFourthBinding
import eramo.amtalek.domain.model.property.addpropertymodels.AddPropertyFourthModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.addproperty.fifth.AddPropertyFifthFragmentArgs
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.navOptionsFromTopAnimation

@AndroidEntryPoint
class AddPropertyFourthFragment : BindingFragment<FragmentAddPropertyFourthBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyFourthBinding::inflate

    val args by navArgs<AddPropertyFourthFragmentArgs>()
    private val thirdModel  get() =  args.thirdModel

    private var imageUri: Uri? = null
    private val uris = mutableListOf<Uri>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        clickListeners()
        Log.e("ARGGG3", thirdModel.toString(), )
    }

    private fun clickListeners() {
        binding.apply {
            primaryImageLayout.setOnClickListener(){
                ImagePicker.with(requireActivity())
                    .compress(1024)
                    .cropSquare()
                    .createIntent { intent ->
                        startForPrimaryImageResult.launch(intent)
                    }
            }
            sliderImagesLayout.setOnClickListener(){
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                startForSliderImageResult.launch(intent)
            }
            btnNext.setOnClickListener(){
                if (formValidation()){
                    val fourthModel = AddPropertyFourthModel(
                        addPropertyThirdModel = thirdModel,
                        descriptionInEnglish = etDescriptionEn.text.toString(),
                        descriptionInArabic = etDescriptionAr.text.toString(),
                        sliderImages = uris,
                        primaryImage = imageUri!!,
                    )

                    findNavController().navigate(R.id.addPropertyFifthFragment,AddPropertyFifthFragmentArgs(fourthModel).toBundle(),
                        navOptionsFromTopAnimation()
                    )
                }else{
                    return@setOnClickListener
                }
            }
        }
    }
    private val startForPrimaryImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                imageUri = fileUri
                binding.ivImagePicked.setImageURI(fileUri)
                binding.ivImagePicked.isVisible = true
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No Image chosen", Toast.LENGTH_SHORT).show()
            }
        }
    private val startForSliderImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the returned Uri list
            if (result.resultCode == Activity.RESULT_OK) {
                val clipData = result.data?.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        uris.add(clipData.getItemAt(i).uri)
                    }
                } else {
                    result.data?.data?.let { uris.add(it) }
                }
                binding.ivSliderImagePicked.setImageURI(uris[0])
                binding.ivSliderImagePicked.isVisible = true
                binding.sliderCountTv.visibility = View.VISIBLE
                if(LocalUtil.isEnglish()){
                    binding.sliderCountTv.text = "${uris.size} Images added"
                }else{
                    binding.sliderCountTv.text = "${uris.size} صور ضيفت "
                }
        }


}
    private fun setupToolbar() {
            binding.inToolbar.apply {
                tvTitle.text = getString(R.string.add_property_4_5)
                ivBack.setOnClickListener { findNavController().popBackStack()}
            }
        }
    private fun formValidation():Boolean{
        var isValid = true
        binding.apply {
            if (imageUri==null){

                isValid = false
            }
            if (uris.isEmpty()){

                isValid = false
            }
            if (etDescriptionEn.text.toString().isNullOrEmpty()){
                etDescriptionEn.error = getString(R.string.please_enter_a_description)
                isValid = false
            }
            if (etDescriptionAr.text.toString().isNullOrEmpty()){
                etDescriptionAr.error = getString(R.string.please_enter_a_description)
                isValid = false
            }
        }
        if (!isValid){
            Toast.makeText(requireContext(), getString(R.string.please_complete_all_fields), Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

}