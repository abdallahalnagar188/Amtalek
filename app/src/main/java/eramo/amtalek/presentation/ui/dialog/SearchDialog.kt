package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.DialogSearchBinding
import eramo.amtalek.util.LocalUtil

@AndroidEntryPoint
class SearchDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LocalUtil.loadLocal(requireActivity())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogSearchBinding.bind(view)

//        dialog?.setOnShowListener {
//            val dialog = it as BottomSheetDialog
//            val bottomSheet = dialog.findViewById<View>(R.id.bottomSheet_question)
//            bottomSheet?.let { sheet ->
//                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                sheet.parent.parent.requestLayout()
//            }
//        }

        binding.apply {

        }
    }
}