package eramo.amtalek.presentation.ui.drawer.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentRateBottomDialogBinding

@AndroidEntryPoint
class RateBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRateBottomDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRateBottomDialogBinding.bind(view)

        binding.btnSendNotes.setOnClickListener { dismiss() }
    }
}