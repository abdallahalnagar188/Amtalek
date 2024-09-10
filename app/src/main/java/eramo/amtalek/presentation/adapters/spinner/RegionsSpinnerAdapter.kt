package eramo.amtalek.presentation.adapters.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import eramo.amtalek.databinding.CitySpinnerItemBinding
import eramo.amtalek.databinding.ItemDropdownBinding
import eramo.amtalek.databinding.RegionSpinnerItemBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.RegionModel

class RegionsSpinnerAdapter(context: Context, list: List<RegionModel>) :
    ArrayAdapter<RegionModel>(context, 0, list) {

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

//            if (getItem(position)?.countryName == "City") {
//                LSpinnerCvFlag.visibility = View.GONE
//            } else {
//                LSpinnerCvFlag.visibility = View.VISIBLE
//            }

            name.text = getItem(position)?.name?:"empty"
//            Glide.with(context).load(getItem(position)?.countryFlag).into(LSpinnerIvFlag)
        }

        return row
    }
}