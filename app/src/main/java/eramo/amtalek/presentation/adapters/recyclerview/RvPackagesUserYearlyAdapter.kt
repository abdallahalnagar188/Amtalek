package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPackagesUserBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import javax.inject.Inject


class RvPackagesUserYearlyAdapter @Inject constructor() :
    ListAdapter<PackageModel, RvPackagesUserYearlyAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: UserYearlyClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPackagesUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPackagesUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnSelect.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onUserYearlyClick(it)
                    }
                }
            }
        }

        fun bind(model: PackageModel) {
            binding.apply {
                tvTitle.text = model.name

                tvDescription.text = model.subTitle
                tvPrice.text = model.priceYearly
                tvDuration.text = itemView.context.getString(R.string.egp_yearly)


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
                }else if ( model.packageType=="free"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.green))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.green))
                }


                btnSelect.setOnClickListener {
                    listener.onUserYearlyClick(model)
                }
            }
        }
    }

    fun setListener(listener: UserYearlyClickListener) {
        this.listener = listener
    }

    interface UserYearlyClickListener {
        fun onUserYearlyClick(model: PackageModel)
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