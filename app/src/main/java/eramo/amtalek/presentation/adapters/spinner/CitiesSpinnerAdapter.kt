package eramo.amtalek.presentation.adapters.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import eramo.amtalek.databinding.CitySpinnerItemBinding
import eramo.amtalek.domain.model.dummy.CountriesSpinnerModel


class CitiesSpinnerAdapter(context: Context, list: List<CountriesSpinnerModel>) :
    ArrayAdapter<CountriesSpinnerModel>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CitySpinnerItemBinding
        var row = convertView

        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = CitySpinnerItemBinding.inflate(inflater, parent, false)
            row = binding.root
        } else {
            binding = CitySpinnerItemBinding.bind(row)
        }

        binding.apply {

//            if (getItem(position)?.countryName == "City") {
//                LSpinnerCvFlag.visibility = View.GONE
//            } else {
//                LSpinnerCvFlag.visibility = View.VISIBLE
//            }

            LSpinnerTvCountryName.text = getItem(position)?.countryName
            Glide.with(context).load(getItem(position)?.countryFlag).into(LSpinnerIvFlag)
        }

        return row
    }
}