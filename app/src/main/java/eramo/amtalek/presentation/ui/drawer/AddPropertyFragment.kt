package eramo.amtalek.presentation.ui.drawer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyCheckboxAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyDescriptionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummySelectedImageAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.SliderZoomFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import javax.inject.Inject

@AndroidEntryPoint
class AddPropertyFragment : BindingFragment<FragmentAddPropertyBinding>(),
    DummySelectedImageAdapter.OnItemClickListener,
    DummyDescriptionAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private var selectedImgs: ArrayList<Uri>? = null
    private var descriptionList: ArrayList<String>? = null
    private var descriptionCount = 0

    @Inject
    lateinit var dummySelectedImageAdapter: DummySelectedImageAdapter

    @Inject
    lateinit var dummyDescriptionAdapter: DummyDescriptionAdapter

    @Inject
    lateinit var dummyCheckboxAdapter: DummyCheckboxAdapter

    private val imgsPickerResultContract = object : ActivityResultContract<Any, ArrayList<Uri>?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            val intent = Intent().apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                action = Intent.ACTION_GET_CONTENT
            }
            return Intent.createChooser(intent, getString(R.string.select_images))
        }

        override fun parseResult(resultCode: Int, intent: Intent?): ArrayList<Uri>? {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val selectedImages = ArrayList<Uri>()

                if (intent?.clipData != null) {
                    val count: Int = intent.clipData!!.itemCount
                    for (i in 0 until count) {
                        selectedImages.add(intent.clipData!!.getItemAt(i).uri)
                    }
                    return selectedImages
                }

                intent?.data?.let { imgUri ->
                    selectedImages.add(imgUri)
                    return selectedImages
                }
            }
            return null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()
        initSpinners()

        val imgsPickerResultLauncher =
            registerForActivityResult(imgsPickerResultContract) { images ->
                images?.let {
                    selectedImgs = it
                    dummySelectedImageAdapter.submitList(it)
                    binding.tvSelectImages.isVisible = false
                }
            }

        dummySelectedImageAdapter.setListener(this)
        dummyDescriptionAdapter.setListener(this)
        binding.apply {
            dummyCheckboxAdapter.submitList(Dummy.dummyProperties())
            rvCheckbox.adapter = dummyCheckboxAdapter

            rvImages.adapter = dummySelectedImageAdapter
            tvSelectImages.isVisible = (selectedImgs.isNullOrEmpty())


            ivPickImages.setOnClickListener { imgsPickerResultLauncher.launch(null) }

            rvDescription.adapter = dummyDescriptionAdapter
            linAddDescription.setOnClickListener {
                if (descriptionList == null) descriptionList = ArrayList()
                descriptionList?.add("$descriptionCount")
                dummyDescriptionAdapter.submitList(descriptionList)
                dummyDescriptionAdapter.notifyItemInserted(descriptionCount++)
            }

            tvLocationOnMap.setOnClickListener {
                findNavController().navigate(R.id.mapFragment, null, navOptionsAnimation())
            }

            tvSubmit.setOnClickListener {
                showToast(getString(R.string.added_successfully))
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener {
                viewModelShared.openDrawer.value = true
            }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initSpinners() {
        binding.apply {
            inSpinnerStatus.root.setBackgroundResource(R.drawable.shape_gray)
        }
    }

    override fun onImageClick(model: Uri) {
        findNavController().navigate(
            R.id.sliderZoomFragment,
            SliderZoomFragmentArgs(imageUri = model.toString()).toBundle()
        )

    }

    override fun onRemoveClick(position: Int) {
        selectedImgs?.removeAt(position)
        dummySelectedImageAdapter.notifyItemRemoved(position)
        binding.tvSelectImages.isVisible = (selectedImgs?.size!! < 1)
    }

    override fun onDeleteClick(position: Int) {
        descriptionList?.removeAt(position)
        dummyDescriptionAdapter.notifyItemRemoved(position)
    }

}