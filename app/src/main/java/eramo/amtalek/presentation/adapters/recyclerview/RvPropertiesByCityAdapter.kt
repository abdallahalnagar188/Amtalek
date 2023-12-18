package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemLatestProjectsBinding
import eramo.amtalek.databinding.ItemSeeMorePropertiesByCityBinding
import eramo.amtalek.domain.model.drawer.latestprojects.LatestProjectsModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import javax.inject.Inject


class RvPropertiesByCityAdapter @Inject constructor() :
    ListAdapter<PropertiesByCityModel, RvPropertiesByCityAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemSeeMorePropertiesByCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemSeeMorePropertiesByCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onCityClick(it)
                    }
                }
            }
        }

        fun bind(model: PropertiesByCityModel) {
            binding.apply {
                tvCityName.text = model.cityName
                tvPropertiesForRent.text =
                    itemView.context.getString(R.string.s_properties_for_rent, model.forRentCount.toString())
                tvPropertiesForSell.text =
                    itemView.context.getString(R.string.s_properties_for_sell, model.forSellCount.toString())

                Glide.with(itemView).load(model.imageUrl).placeholder(R.drawable.ic_no_image).into(ivImage)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onCityClick(model: PropertiesByCityModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<PropertiesByCityModel>() {
            override fun areItemsTheSame(
                oldItem: PropertiesByCityModel,
                newItem: PropertiesByCityModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PropertiesByCityModel,
                newItem: PropertiesByCityModel
            ) = oldItem == newItem
        }
    }
}