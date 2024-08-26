package eramo.amtalek.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.hideSoftKeyboard

abstract class BindingFragment<out T : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected open val binding: T
        get() = _binding as T


    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding
    private var isInitializeScreen = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().hideSoftKeyboard()
        LocalUtil.loadLocal(requireActivity())
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
}