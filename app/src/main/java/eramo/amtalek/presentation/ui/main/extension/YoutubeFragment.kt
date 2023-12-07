package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import eramo.amtalek.databinding.FragmentYoutubeBinding
import eramo.amtalek.presentation.ui.BindingFragment


class YoutubeFragment : BindingFragment<FragmentYoutubeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentYoutubeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//         viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayerView)
//        binding.apply {
//            lifecycle.addObserver(youtubeView)
//            youtubeView.addYouTubePlayerListener(object :
//                AbstractYouTubePlayerListener() {
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    super.onReady(youTubePlayer)
//                    onVideoId(youTubePlayer, "uCFANEq7h9s")
//                    youTubePlayer.loadVideo("uCFANEq7h9s", 0f)
//                    youTubePlayer.play()
//                }
//            })
//
//
//        }
    }
}