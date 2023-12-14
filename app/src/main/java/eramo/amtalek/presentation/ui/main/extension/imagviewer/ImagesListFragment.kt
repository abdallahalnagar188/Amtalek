package eramo.amtalek.presentation.ui.main.extension.imagviewer

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentImagesListBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvImagesListAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class ImagesListFragment : BindingFragment<FragmentImagesListBinding>(), RvImagesListAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentImagesListBinding::inflate

    @Inject
    lateinit var rvImagesListAdapter: RvImagesListAdapter

    private val args by navArgs<ImagesListFragmentArgs>()
    private val imagesList get() = args.imagesList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {

        initRv(imagesList.toList())
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initRv(data: List<String>) {
        rvImagesListAdapter.setListener(this@ImagesListFragment)
        binding.rv.adapter = rvImagesListAdapter
        rvImagesListAdapter.submitList(data)
    }

    override fun onImageClick(model: Bitmap) {
        findNavController().navigate(R.id.imageViewFragment, ImageViewFragmentArgs(model).toBundle(), navOptionsAnimation())
    }
}