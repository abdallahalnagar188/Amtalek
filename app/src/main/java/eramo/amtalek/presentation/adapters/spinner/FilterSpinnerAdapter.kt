package eramo.amtalek.presentation.adapters.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import eramo.amtalek.databinding.ItemDropdownBinding
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.model.property.CriteriaModel

class FilterSpinnerAdapter(context: Context, list: List<String>) :
    ArrayAdapter<String>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemDropdownBinding
        var row = convertView

        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = ItemDropdownBinding.inflate(inflater, parent, false)
            row = binding.root
        } else {
            binding = ItemDropdownBinding.bind(row)
        }

        binding.apply {
            name.text = getItem(position).toString()
        }

        return row
    }
}