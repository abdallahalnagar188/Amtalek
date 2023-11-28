package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.HomeFilterCitiesItemBinding
import eramo.amtalek.domain.model.auth.CityModel
import javax.inject.Inject


class RvHomeFilterCities @Inject constructor() :
    ListAdapter<CityModel, RvHomeFilterCities.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        HomeFilterCitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: HomeFilterCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                selectedPosition = bindingAdapterPosition
                notifyDataSetChanged()

                getItem(selectedPosition).let {
                    listener.onItemClick(it)
                }
            }
        }

        fun bind(model: CityModel) {
            binding.apply {
                tv.text = model.name

                Glide.with(itemView.context).load(R.drawable.ic_city).into(iv)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(model: CityModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<CityModel>() {
            override fun areItemsTheSame(
                oldItem: CityModel,
                newItem: CityModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: CityModel,
                newItem: CityModel
            ) = oldItem == newItem
        }
    }
}