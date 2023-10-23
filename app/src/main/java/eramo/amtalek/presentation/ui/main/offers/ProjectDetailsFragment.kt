package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentProjectDetailsBinding
import eramo.amtalek.presentation.adapters.BannerSliderAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyAlbumAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyAutocadAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.SliderZoomFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.ShopViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class ProjectDetailsFragment : BindingFragment<FragmentProjectDetailsBinding>(),
    BannerSliderAdapter.OnItemClickListener,
    DummyAlbumAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProjectDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()

    @Inject
    lateinit var dummyAlbumAdapter: DummyAlbumAdapter

    @Inject
    lateinit var dummyAutocadAdapter: DummyAutocadAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        setupSlider()
        setupAlbum()
        setupVideo()
        binding.apply {
            dummyAutocadAdapter.submitList(Dummy.list())
            rvAutocad.adapter = dummyAutocadAdapter

            tvLocationOnMap.setOnClickListener {
                findNavController().navigate(R.id.mapFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupSlider() {
        binding.apply {
            val bannerSliderAdapter = BannerSliderAdapter()
            bannerSliderAdapter.renewItems(Dummy.dummyAlbumList())
            bannerSliderAdapter.setListener(this@ProjectDetailsFragment)
            sliderAlbums.setSliderAdapter(bannerSliderAdapter)
            sliderAlbums.setCurrentPageListener {
                rvAlbum.smoothScrollToPosition(it)
                dummyAlbumAdapter.setCurrentPosition(it)
            }
        }
    }

    private fun setupAlbum() {
        binding.apply {
            dummyAlbumAdapter.setListener(this@ProjectDetailsFragment)
            dummyAlbumAdapter.submitList(Dummy.dummyAlbumList())
            rvAlbum.adapter = dummyAlbumAdapter
        }
    }

    private fun setupVideo() {
        val videoId = "zo40BGfu5Gg"
        binding.apply {
            lifecycle.addObserver(FAboutUsYoutubeView)
            FAboutUsYoutubeView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    onVideoId(youTubePlayer, videoId)
                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.pause()
                }
            })
        }
    }

    override fun onImgClick(position: Int) {
        binding.apply {
            sliderAlbums.currentPagePosition = position
        }
    }

    override fun onBannerClick(position: Int) {
        findNavController().navigate(
            R.id.sliderZoomFragment,
            SliderZoomFragmentArgs(
                dummyAlbumAdapter.currentList.toTypedArray(),
                position
            ).toBundle()
        )
    }
}