package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemAdminitesRvBinding
import eramo.amtalek.databinding.ItemAminitiesBinding
import eramo.amtalek.domain.model.project.AmenityModel
import javax.inject.Inject


class RvAmenitiesAdapter @Inject constructor() :
    ListAdapter<AmenityModel, RvAmenitiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemAminitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemAminitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: AmenityModel) {
            binding.apply {
                amenityName.text = model.name
                Glide.with(itemView.context)
                    .load(model.image)
                    .placeholder(R.drawable.ic_no_image)
                    .into(icon)

            }
        }
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<AmenityModel>() {
            override fun areItemsTheSame(
                oldItem: AmenityModel,
                newItem: AmenityModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AmenityModel,
                newItem: AmenityModel
            ) = oldItem == newItem
        }
    }
}