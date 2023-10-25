package eramo.amtalek.presentation.ui.main.extension.imagviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentImageViewBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class ImageViewFragment : BindingFragment<FragmentImageViewBinding>() {
    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentImageViewBinding::inflate

    private val args by navArgs<ImageViewFragmentArgs>()
    private val image get() = args.image

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        listeners()
    }

    private fun setupViews() {
        StatusBarUtil.hide()
        binding.ivImage.setImageBitmap(image)
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }
}