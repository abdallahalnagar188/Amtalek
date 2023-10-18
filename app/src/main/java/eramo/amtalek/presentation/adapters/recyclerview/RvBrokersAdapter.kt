package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemBrokerBinding
import eramo.amtalek.domain.model.main.brokers.BrokerModel
import javax.inject.Inject

class RvBrokersAdapter @Inject constructor() :
    ListAdapter<BrokerModel, RvBrokersAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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

        fun bind(model: BrokerModel) {
            binding.apply {
                tvTitle.text = model.title
                tvBody.text = model.body
                tvResidentialProjects.text =
                    itemView.context.getString(R.string.s_residential_projects, model.residentialProjectsCount.toString())
                tvProperty.text = itemView.context.getString(R.string.s_property, model.propertiesCount.toString())

                Glide.with(itemView).load(model.imageUrl).into(ivBrokerLogo)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onBrokerClick(model: BrokerModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<BrokerModel>() {
            override fun areItemsTheSame(
                oldItem: BrokerModel,
                newItem: BrokerModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: BrokerModel,
                newItem: BrokerModel
            ) = oldItem == newItem
        }
    }
}