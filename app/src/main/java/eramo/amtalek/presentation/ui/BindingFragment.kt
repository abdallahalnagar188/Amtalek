package eramo.amtalek.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import eramo.amtalek.R
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.hideSoftKeyboard
import kotlinx.coroutines.delay

abstract class BindingFragment<out T : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T
    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding
    protected abstract val isRefreshingEnabled: Boolean
    private var swiper: SwipeRefreshLayout? = null
    private var isInitializeScreen = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().hideSoftKeyboard()
        LocalUtil.loadLocal(requireActivity())
        swiper = (requireActivity() as MainActivity).findViewById(R.id.swiper)
        swiper?.isEnabled = isRefreshingEnabled
        _binding = bindingInflater(inflater)
        return _binding!!.root
    }

    override fun onStop() {
        super.onStop()
        LoadingDialog.cancelCurrentRequest.value = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun registerApiRequest(apiRequest: () -> Unit) {
        if (isInitializeScreen) {
            apiRequest()
            isInitializeScreen = false
        }
        swiper?.setOnRefreshListener {
//            apiRequest()
            lifecycleScope.launchWhenStarted {
                LoadingDialog.showDialog()
                delay(2000L)
                LoadingDialog.dismissDialog()
                swiper?.isRefreshing=false
            }
        }
    }

    protected fun registerApiCancellation(cancelApiRequest: () -> Unit) {
        LoadingDialog.cancelCurrentRequest.observe(viewLifecycleOwner) { isCancel ->
            if (isCancel) {
                cancelApiRequest()
                dismissLoading()
                LoadingDialog.cancelCurrentRequest.value = false
            }
        }
    }

    protected fun showLoading() {
        if (!swiper?.isRefreshing!!) LoadingDialog.showDialog()
    }

    protected fun dismissLoading() {
        swiper?.isRefreshing = false
        LoadingDialog.dismissDialog()
    }
}