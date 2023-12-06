package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import eramo.amtalek.R
import eramo.amtalek.databinding.DialogDatePickerBinding
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import java.util.Calendar

class DatePickerDialog : DialogFragment(R.layout.dialog_date_picker) {

    private lateinit var binding: DialogDatePickerBinding
    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogDatePickerBinding.bind(view)

        binding.apply {
            val today = Calendar.getInstance()
//            datePicker.maxDate = Date().time
            datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ) { _, _, _, _ -> }

            DFDatePickerBtnCancel.setOnClickListener { findNavController().popBackStack() }

            DFDatePickerBtnSelect.setOnClickListener {
                val date = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                viewModelShared.dateString.value = date
                findNavController().popBackStack()
            }
        }
    }
}