package eramo.amtalek.presentation.adapters.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import eramo.amtalek.databinding.CitySpinnerItemBinding
import eramo.amtalek.databinding.ItemDropdownBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.util.LocalUtil


class CitiesSpinnerAdapter(context: Context, list: List<CityModel>) :
    ArrayAdapter<CityModel>(context, 0, list) {

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
            if (LocalUtil.isEnglish()){
                name.text = getItem(position)?.titleEn
            }else{
                name.text = getItem(position)?.titleAr
            }
//            Glide.with(context).load(getItem(position)?.countryFlag).into(LSpinnerIvFlag)
        }

        return row
    }
}