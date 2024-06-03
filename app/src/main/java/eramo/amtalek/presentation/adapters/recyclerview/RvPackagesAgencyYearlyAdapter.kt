package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPackagesBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import javax.inject.Inject


class RvPackagesAgencyYearlyAdapter @Inject constructor() :
    ListAdapter<PackageModel, RvPackagesAgencyYearlyAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: AgencyYearlyClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPackagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPackagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnSelect.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onPlanClick(it)
                    }
                }
            }
        }

        fun bind(model: PackageModel) {
            binding.apply {
                tvTitle.text = model.packageType
                tvDescription.text = model.subTitle
                tvPrice.text = model.priceYearly
                tvDuration.text = itemView.context.getString(R.string.egp_yearly)


                tvNormalListing.text = itemView.context.getString(R.string.s_normal_listing, model.normalListings)
                tvFeaturedListing.text = itemView.context.getString(R.string.s_featured_listings, model.featuredListings)
                tvMoney.text = itemView.context.getString(R.string.e_money_s, model.emoney)
                tvMessages.text = itemView.context.getString(R.string.messages_s, model.messages)
                tvLeadsManagement.text = itemView.context.getString(R.string.s_leads_management, model.leadsManagement)
                tvProjects.text = itemView.context.getString(R.string.projects_s, model.projects)
                tvCrmAgents.text = itemView.context.getString(R.string.s_crm_agents, model.crmAgents)
                tvHrModule.text = itemView.context.getString(R.string.s_hr_module, model.hrModule)
                tvAccountingModule.text = itemView.context.getString(R.string.s_accounting_module, model.accountingModule)


                if (model.packageType=="normal"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.amtalek_blue))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.amtalek_blue))
                }else if ( model.packageType=="featured"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.amtalek_red))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.amtalek_red))
                }


                btnSelect.setOnClickListener {
                    listener.onAgencyYearlyPlanClick(model)
                }
            }
        }
    }

    fun setListener(listener: AgencyYearlyClickListener) {
        this.listener = listener
    }
    interface AgencyYearlyClickListener {
        fun onAgencyYearlyPlanClick(model: PackageModel)
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