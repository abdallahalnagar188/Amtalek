package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemFindYourPropertyByCityBinding
import eramo.amtalek.domain.model.home.cities.CitiesModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import javax.inject.Inject


class RvHomeFindPropertyByCityAdapter @Inject constructor() :
    ListAdapter<CitiesModel, RvHomeFindPropertyByCityAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemFindYourPropertyByCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemFindYourPropertyByCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onPropertyByCityClick(it)
                    }
                }
            }
        }

        fun bind(model: CitiesModel) {
            binding.apply {

                tvCityName.text = model.title
                tvPropertiesForRent.text =
                    itemView.context.getString(R.string.s_properties_for_rent, model.rentProperties.toString())
                tvPropertiesForSell.text =
                    itemView.context.getString(R.string.s_properties_for_sell, model.salesProperties.toString())

                Glide.with(itemView).load(model.image).placeholder(R.drawable.ic_no_image).into(ivImage)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPropertyByCityClick(model: CitiesModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<CitiesModel>() {
            override fun areItemsTheSame(
                oldItem: CitiesModel,
                newItem: CitiesModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: CitiesModel,
                newItem: CitiesModel
            ) = oldItem == newItem
        }
    }
}