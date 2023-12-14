package eramo.amtalek.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSliderZoomBinding
import eramo.amtalek.domain.model.dummy.AlbumModel
import eramo.amtalek.presentation.adapters.ZoomSliderAdapter
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class SliderZoomFragment : BindingFragment<FragmentSliderZoomBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSliderZoomBinding::inflate

    private val args by navArgs<SliderZoomFragmentArgs>()
    private val images get() = args.images
    private val position get() = args.position
    private val imageUri get() = args.imageUri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.transparent()

        setupSlider()
        binding.apply {
            ivClose.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupSlider() {
        val zoomSliderAdapter = ZoomSliderAdapter()
        binding.slider.apply {
            setInfiniteAdapterEnabled(false)

            images?.let { list ->
                zoomSliderAdapter.renewItems(list.toList())
                setSliderAdapter(zoomSliderAdapter)
                currentPagePosition = position
            }

            imageUri?.let { uri ->
                val albumModel = AlbumModel(imgUrl = uri)
                zoomSliderAdapter.renewItems(listOf(albumModel))
                setSliderAdapter(zoomSliderAdapter)
            }
        }
    }
}