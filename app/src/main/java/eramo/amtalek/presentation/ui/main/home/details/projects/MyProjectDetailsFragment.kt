package eramo.amtalek.presentation.ui.main.home.details.projects

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.project.Aminity
import eramo.amtalek.data.remote.dto.project.Autocad
import eramo.amtalek.data.remote.dto.project.Data
import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.data.remote.dto.project.Slider
import eramo.amtalek.databinding.FragmentMyProjectDetailsBinding
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.getYoutubeUrlId
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class MyProjectDetailsFragment : BindingFragment<FragmentMyProjectDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyProjectDetailsBinding::inflate

    val viewModel: ProjectDetailsViewModel by viewModels()
    private val args: MyProjectDetailsFragmentArgs by navArgs()
    val listingNumber  get() = args.projectId
    lateinit var vendorId:String

    @Inject
    lateinit var amenitiesAdapter: AmenitiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProjectDetails(listingNumber)
        setupViews()
        fetchData()
        clicks()

    }

    private fun clicks() {
        binding.btnSend.setOnClickListener()    {
            if (validForm()){
                binding.apply {
                    val name = etName.text.toString()
                    val email = etMail.text.toString()
                    val phone = etPhone.text.toString()
                    val message = etNotes.text.toString()
                    viewModel.sendToBroker(
                        name = name,
                        email =email,
                        phone = phone,
                        message = message,
                        vendorId =vendorId
                        )
                }
            }else{
                Toast.makeText(requireContext(), "finish the form", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun validForm():Boolean{
        var isValid = true
        binding.apply {
            if (etName.text.toString().isEmpty()){
                isValid = false
                etName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etMail.text.toString())){
                isValid = false
                etMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etPhone.text.toString().isEmpty()){
                etPhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etNotes.text.toString().isEmpty()){
                etNotes.error = getString(R.string.please_enter_a_message)
                isValid = false
            }
        }
        return isValid
    }
    private fun fetchData() {
        fetchProjectDetails()
        fetchSendMessageState()
    }
    private fun fetchSendMessageState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                withContext(coroutineContext){
                    viewModel.sentToBrokerState.collect{
                        when(it){
                            is UiState.Success ->{
                                Toast.makeText(requireContext(), getString(R.string.message_sent_successfully), Toast.LENGTH_SHORT).show()
                                LoadingDialog.dismissDialog()
                            }
                            is UiState.Error ->{

                                LoadingDialog.dismissDialog()
                            }
                            is UiState.Loading ->{
                                LoadingDialog.showDialog()

                            }
                            is UiState.Empty ->Unit
                        }
                    }
                }
            }
        }
    }
    private fun fetchProjectDetails() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                withContext(coroutineContext){
                    viewModel.projectDetailsState.collect{
                        when(it){
                            is UiState.Success ->{
                                val data = it.data
                                handleSuccessState(data)

                                LoadingDialog.dismissDialog()
                            }
                            is UiState.Error ->{

                                LoadingDialog.dismissDialog()
                            }
                            is UiState.Loading ->{
                                LoadingDialog.showDialog()

                            }

                            is UiState.Empty ->Unit
                        }
                    }
                }
            }
        }
    }
    private fun handleSuccessState(data: ProjectDetailsResponse?) {
        val topSliderImages = handleImageTopSlider(data?.data?.get(0)?.sliders)
        setupImageSliderTop(topSliderImages)

        val autocadImages = handleAutocadImages(data?.data?.get(0)?.autocad)
        setupAutocadImagesSlider(autocadImages)

        val amenities = handleAmenities(data?.data?.get(0)?.aminities)
        binding.projectFeaturesLayout.recyclerView.adapter = amenitiesAdapter
        amenitiesAdapter.saveData(amenities)

        mapSetup(data?.data?.get(0)?.location)


        getYoutubeUrlId(data?.data?.get(0)?.video!!)?.let {
            setupVideo(it)
        }


        injectData(data?.data?.get(0))

        vendorId = data?.data?.get(0)?.brokerDetails?.get(0)?.id.toString()
        
    }
    private fun mapSetup(data: String?) {
        val video = data
        video?.let {
            binding.webView.settings.javaScriptEnabled =true
            binding.webView.webChromeClient = WebChromeClient()
            binding.webView.loadData(video,"text/html","utf-8")

        }
    }
    private fun setupVideo(videoId: String) {
        binding.apply {
            lifecycle.addObserver(youtubePlayerView)
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    onVideoId(youTubePlayer, videoId)
                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.pause()
                }
            })
        }
    }

    private fun handleAmenities(aminities: List<Aminity?>?): List<AmenityModel> {
        val amenityModelList :ArrayList<AmenityModel> = ArrayList()
        aminities?.let {
            for (amenity in aminities){
                amenityModelList.add(amenity!!.toAmenityModel())
            }
        }
        return amenityModelList
    }
    private fun injectData(data: Data?) {
        binding.apply {
            tvPrice.text = data?.startPrice.toString()
            tvTitle.text = data?.name
            tvLocation.text = data?.quickSummary?.address
            tvDate.text = data?.deliveryDate
            tvDescriptionValue.text = data?.description
            tvUserName.text = data?.brokerDetails?.get(0)?.name
            tvUserId.text = data?.brokerDetails?.get(0)?.description
            Glide.with(requireContext()).load(data?.brokerDetails?.get(0)?.logo)
                .into(ivUserImage)

        }
    }
    private fun handleAutocadImages(autocad: List<Autocad?>?): List<CarouselItem> {
        val imageList = mutableListOf<CarouselItem>()
        for (image in autocad!!){
            imageList.add(CarouselItem(imageUrl = image?.src))
        }
        return imageList
    }
    private fun handleImageTopSlider(sliders: List<Slider?>?):List<CarouselItem> {
        val imageList = mutableListOf<CarouselItem>()
        for (image in sliders!!){
            imageList.add(CarouselItem(imageUrl = image?.src))
        }
        return imageList
    }
    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }
    private fun setupViews() {
        setupToolbar()
        binding.tvTitle.setOnClickListener {
            findNavController().navigate(R.id.mapFragment, null, navOptionsAnimation())
        }
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupImageSliderTop(data: List<CarouselItem>) {
        binding.apply {
            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            imageSlider.setIndicator(imageSliderDots)
            imageSlider.setData(data)
        }
    }

    private fun setupAutocadImagesSlider(data: List<CarouselItem>) {
        binding.apply {
            autocadDrawingsSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            autocadDrawingsSlider.setIndicator(autocadDrawingsSliderDots)
            autocadDrawingsSlider.setData(data)
        }
    }

}