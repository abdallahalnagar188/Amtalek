package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPackagesUserBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.util.UserUtil
import javax.inject.Inject


class RvPackagesUserMonthlyAdapter @Inject constructor() :
    ListAdapter<PackageModel, RvPackagesUserMonthlyAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: UserMonthlyClickListener

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
                       listener.onUserMonthlyClick(it)
                    }
                }
            }
        }

        fun bind(model: PackageModel) {
            binding.apply {
                tvTitle.text = model.name
                val userPackageId = UserUtil.packageIdForUser()

                if (!userPackageId.isNullOrBlank()) {
                    val parsedPackageId = userPackageId.toIntOrNull()

                    if (parsedPackageId != null && model.id == parsedPackageId) {
                        tvAddProperty.text = itemView.context.getString(R.string.my_plan)
                    } else {
                        tvAddProperty.text = itemView.context.getString(R.string.select_this_plan)
                    }
                } else {
                    // Handle case where the package ID is invalid or not set
                    tvAddProperty.text = itemView.context.getString(R.string.select_this_plan)
                }
                tvDescription.text = model.subTitle
                tvPrice.text = model.priceMonthly
                tvDuration.text = itemView.context.getString(R.string.egp_month)

                tvNormalListing.text = itemView.context.getString(R.string.s_normal_listing, model.normalListings)
                tvFeaturedListing.text = itemView.context.getString(R.string.s_featured_listings, model.featuredListings)
                tvMoney.text = itemView.context.getString(R.string.e_money_s, model.emoney)
                tvMessages.text = itemView.context.getString(R.string.messages_s, model.messages)



                if (model.normalListings == "0"){
                    ivRightMark.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }
                if (model.featuredListings=="0"){
                    ivRightMark2.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }

                if (model.messages=="0"){
                    ivRightMark3.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))
                }
                if (model.emoney=="0.0"){
                    ivRightMark4.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))
                }


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
                    listener.onUserMonthlyClick(model)
                }
            }
        }
    }

    fun setListener(listener: UserMonthlyClickListener) {
        this.listener = listener
    }

    interface UserMonthlyClickListener {
        fun onUserMonthlyClick(model: PackageModel)
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