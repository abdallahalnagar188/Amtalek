package eramo.amtalek.presentation.adapters.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import eramo.amtalek.databinding.ItemDropdownBinding
import eramo.amtalek.databinding.ItemDropdownSmallBinding
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.model.property.CriteriaModel

class CriteriaSpinnerSmallAdapter(context: Context, list: List<CriteriaModel>) :
    ArrayAdapter<CriteriaModel>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemDropdownSmallBinding
        var row = convertView

        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = ItemDropdownSmallBinding.inflate(inflater, parent, false)
            row = binding.root
        } else {
            binding = ItemDropdownSmallBinding.bind(row)
        }

        binding.apply {

//            if (getItem(position)?.countryName == "City") {
//                LSpinnerCvFlag.visibility = View.GONE
//            } else {
//                LSpinnerCvFlag.visibility = View.VISIBLE
//            }

            name.text = getItem(position)?.title
//            Glide.with(context).load(getItem(position)?.countryFlag).into(LSpinnerIvFlag)
        }

        return row
    }
}