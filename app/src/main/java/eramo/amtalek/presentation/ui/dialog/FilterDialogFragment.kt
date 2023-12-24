package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentFilterDialogBinding

@AndroidEntryPoint
class FilterDialogFragment : DialogFragment() {

    private var binding: FragmentFilterDialogBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {

    }

    private fun listeners() {
        binding?.apply {
            ivClose.setOnClickListener {
                dialog?.dismiss()
            }
            btnSearch.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }
}