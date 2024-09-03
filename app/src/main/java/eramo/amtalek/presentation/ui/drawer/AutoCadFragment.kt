package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentAutoCadBinding
import eramo.amtalek.domain.model.project.AutocadModel
import eramo.amtalek.presentation.ui.BindingFragment
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class AutoCadFragment : BindingFragment<FragmentAutoCadBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAutoCadBinding::inflate

    private val args: AutoCadFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (args.autocadList.isEmpty()) {
            binding.autocadDrawingsSlider.visibility = View.GONE
        } else {
            val autocadImages = handleAutocadImages(args.autocadList.toList())
            setupAutocadImagesSlider(autocadImages)
        }
        binding.tvTitle.text = args.title
        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()}
    }

    private fun setupAutocadImagesSlider(data: List<CarouselItem>) {
        binding.apply {
            autocadDrawingsSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            autocadDrawingsSlider.setData(data)
            if (data.size == 1) {
                autocadDrawingsSlider.infiniteCarousel = false
                autocadDrawingsSlider.autoPlay = false
                autocadDrawingsSlider.imageScaleType = ImageView.ScaleType.CENTER_INSIDE
            } else {
                autocadDrawingsSlider.infiniteCarousel = true
                autocadDrawingsSlider.autoPlay = true
                autocadDrawingsSlider.imageScaleType = ImageView.ScaleType.CENTER_INSIDE
            }
        }
    }

    private fun handleAutocadImages(autocad: List<AutocadModel?>): List<CarouselItem> {
        return autocad.mapNotNull { image ->
            image?.src?.let { src -> CarouselItem(imageUrl = src) }
        }
    }
}
