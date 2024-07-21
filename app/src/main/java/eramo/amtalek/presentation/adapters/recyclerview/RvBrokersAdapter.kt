package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.broker.entity.DataX
import eramo.amtalek.databinding.ItemBrokerBinding
import eramo.amtalek.domain.model.main.brokers.BrokerModel
import javax.inject.Inject

class RvBrokersAdapter @Inject constructor() :
    ListAdapter<DataX, RvBrokersAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemBrokerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemBrokerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onBrokerClick(it)
                    }
                }
            }
        }

        fun bind(model: DataX) {
            binding.apply {
                tvTitle.text = model.name
                tvBody.text = model.description
                tvResidentialProjects.text =
                    itemView.context.getString(R.string.s_residential_projects, model.property_for_sale.toString())
                tvProperty.text = itemView.context.getString(R.string.s_property, model.property_for_rent.toString())

                Glide.with(itemView).load(model.logo).into(ivBrokerLogo)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onBrokerClick(model: DataX)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem
        }
    }
}