package eramo.amtalek.presentation.adapters.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import eramo.amtalek.databinding.CountrySpinnerItemBinding
import eramo.amtalek.domain.model.auth.CountryModel


class CountriesSpinnerAdapter(context: Context, list: List<CountryModel>) :
    ArrayAdapter<CountryModel>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CountrySpinnerItemBinding
        var row = convertView

        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = CountrySpinnerItemBinding.inflate(inflater, parent, false)
            row = binding.root
        } else {
            binding = CountrySpinnerItemBinding.bind(row)
        }

        binding.apply {

//            if (getItem(position)?.countryName == "Country") {
//                LSpinnerCvFlag.visibility = View.GONE
//            } else {
//                LSpinnerCvFlag.visibility = View.VISIBLE
//            }

            tvCountryName.text = getItem(position)?.name
//            Glide.with(context).load(getItem(position)?.countryFlag).into(LSpinnerIvFlag)
        }

        return row
    }
}