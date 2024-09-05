package eramo.amtalek.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMapBinding
import eramo.amtalek.presentation.viewmodel.SharedViewModel

@AndroidEntryPoint
class MapFragment : BindingFragment<FragmentMapBinding>(){

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMapBinding::inflate



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url")

        Log.e("url map", url.toString())
        binding.webView.apply {
            url?.let {
                val adjustedData = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                    }
                    iframe, img, video {
                        max-width: 100%;
                        width: 100%;
                        height: 100vh;
                    }
                </style>
                <script>
                    var lastY;
                    var scale = 1;

                    document.addEventListener('touchstart', function(e) {
                        if (e.touches.length == 1) {
                            lastY = e.touches[0].clientY;
                        }
                    });

                    document.addEventListener('touchmove', function(e) {
                        if (e.touches.length == 1) {
                            var currentY = e.touches[0].clientY;
                            var deltaY = currentY - lastY;

                            scale += deltaY * 0.005;  // Adjust the zoom sensitivity as needed
                            scale = Math.max(0.5, Math.min(scale, 3));  // Restrict scale between 0.5 and 3
                            
                            document.body.style.transform = 'scale(' + scale + ')';
                            document.body.style.transformOrigin = '50% 50%';
                            lastY = currentY;
                        }
                    });

                    document.addEventListener('touchend', function(e) {
                        lastY = null;
                    });
                </script>
            </head>
            <body>
                $url
            </body>
            </html>
        """.trimIndent()

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = false  // Disable default zoom controls
                    displayZoomControls = false
                    setSupportZoom(false)  // Disable native zoom support
                }

                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()

                // Load the adjusted HTML data
                loadData(adjustedData, "text/html", "utf-8")
            }
        }
        binding.cvBack.setOnClickListener {
            findNavController().popBackStack() // Navigate back or close the fragment
        }
    }
}
