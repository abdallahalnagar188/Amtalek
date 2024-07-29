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

            if (getItem(0)?.title == "Type") {
                getItem(0)?.title = "All Types"
            }
            if (getItem(0)?.title == "النوع") {
                getItem(0)?.title = "كل الانواع"
            }
            if (getItem(0)?.title == "Currency") {
                getItem(0)?.title = "All Currency"
            }

            if (getItem(0)?.title == "العملة") {
                getItem(0)?.title = "كل العملات"
            }

            if (getItem(0)?.title == "Finishing") {
                getItem(0)?.title = "Completion stages"
            }
            if (getItem(0)?.title == "التشطيب") {
                getItem(0)?.title = "كل مراحل التشطيب"
            }
            if (getItem(0)?.title == "Purpose") {
                getItem(0)?.title = "All Purposes"
            }
            if (getItem(0)?.title == "الغرض") {
                getItem(0)?.title = "كل الاغراض"
            }

            name.text = getItem(position)?.title
//            Glide.with(context).load(getItem(position)?.countryFlag).into(LSpinnerIvFlag)
        }

        return row
    }
}