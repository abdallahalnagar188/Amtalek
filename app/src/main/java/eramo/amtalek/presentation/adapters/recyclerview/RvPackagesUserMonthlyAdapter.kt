package eramo.amtalek.presentation.adapters.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPackagesUserBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import javax.inject.Inject


class RvPackagesUserMonthlyAdapter @Inject constructor() :
    ListAdapter<PackageModel, RvPackagesUserMonthlyAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPackagesUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPackagesUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onSelectClick(it)
                    }
                }
            }
        }

        fun bind(model: PackageModel) {
            binding.apply {
                tvTitle.text = model.packageType
                tvDescription.text = model.subTitle
                tvPrice.text = model.priceMonthly
                tvDuration.text = itemView.context.getString(R.string.egp_month)

                tvNormalListing.text = itemView.context.getString(R.string.s_normal_listing, model.normalListings)
                tvFeaturedListing.text = itemView.context.getString(R.string.s_featured_listings, model.featuredListings)
                tvMoney.text = itemView.context.getString(R.string.e_money_s, model.emoney)
                tvMessages.text = itemView.context.getString(R.string.messages_s, model.messages)


                if (model.packageType=="normal"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.amtalek_blue))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.amtalek_blue))
                }else if ( model.packageType=="featured"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.amtalek_red))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.amtalek_red))
                }


                btnSelect.setOnClickListener {
                    listener.onSelectClick(model)
                }
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onSelectClick(model: PackageModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<PackageModel>() {
            override fun areItemsTheSame(
                oldItem: PackageModel,
                newItem: PackageModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PackageModel,
                newItem: PackageModel
            ) = oldItem == newItem
        }
    }
}