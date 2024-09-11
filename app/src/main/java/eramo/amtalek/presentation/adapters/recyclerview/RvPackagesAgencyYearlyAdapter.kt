package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPackagesBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
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
                        listener.onAgencyYearlyPlanClick(it)
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
                when(model.name){
                    "FREE" -> {
                        tvTitle.text = itemView.context.getString(R.string.free)
                    }
                }

                if(model.leadsManagement == "0"){
                    ivRightMark5.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))
                }
                if (model.normalListings == "0"){
                    ivRightMark.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }
                if (model.featuredListings=="0"){
                    ivRightMark2.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }
                if (model.projects=="0"){
                    ivRightMark6.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }
                if (model.crmAgents=="0"){
                    ivRightMark7.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }
                if (model.hrModule=="no"){
                    ivRightMark9.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }
                if (model.accountingModule=="no"){
                    ivRightMark10.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))


                }
                if (model.supervisors=="0"){
                    ivRightMark8.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))

                }

                if (model.messages=="0"){
                    ivRightMark3.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))
                }
                if (model.emoney=="0.0"){
                    ivRightMark4.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_wrong))
                }

                tvNormalListing.text = itemView.context.getString(R.string.s_normal_listing, model.normalListings)
                tvFeaturedListing.text = itemView.context.getString(R.string.s_featured_listings, model.featuredListings)
                tvMoney.text = itemView.context.getString(R.string.e_money_s, model.emoney)
                tvMessages.text = itemView.context.getString(R.string.messages_s, model.messages)
                tvLeadsManagement.text = itemView.context.getString(R.string.s_leads_management, model.leadsManagement)
                tvProjects.text = itemView.context.getString(R.string.projects_s, model.projects)
                tvCrmAgents.text = itemView.context.getString(R.string.s_crm_agents, model.crmAgents)


                when (model.hrModule) {
                    "yes" -> {
                        val yesText = itemView.context.getString(R.string.yes) // Get the actual string value
                        tvHrModule.text = itemView.context.getString(R.string.s_hr_module, yesText)
                    }
                    "no" -> {
                        val noText = itemView.context.getString(R.string.no) // Get the actual string value
                        tvHrModule.text = itemView.context.getString(R.string.s_hr_module, noText)
                    }
                }

             //   tvHrModule.text = itemView.context.getString(R.string.s_hr_module, model.hrModule)
                when(model.accountingModule){
                    "yes" -> {
                        val yesText = itemView.context.getString(R.string.yes) // Get the actual string value
                        tvAccountingModule.text = itemView.context.getString(R.string.s_accounting_module, yesText)
                    }
                    "no" -> {
                        val noText = itemView.context.getString(R.string.no) // Get the actual string value
                        tvAccountingModule.text = itemView.context.getString(R.string.s_accounting_module, noText)
                    }
                }
               // tvAccountingModule.text = itemView.context.getString(R.string.s_accounting_module, model.accountingModule)
                tvSupervisors.text = itemView.context.getString(R.string.s_supervisors, model.supervisors)


                if (model.packageType=="normal"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.amtalek_blue))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.amtalek_blue))
                }else if ( model.packageType=="featured"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.amtalek_red))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.amtalek_red))
                }else if ( model.packageType=="free"){
                    cover.setBackgroundColor(itemView.context.getColor(R.color.green))
                    cvPrice.setCardBackgroundColor(itemView.context.getColor(R.color.green))
                    tvPrice.text = model.priceYearly
                }
                btnSelect.setOnClickListener {
                    val navController = Navigation.findNavController(itemView) // Get NavController from the itemView

                    // Check if user is logged in
                    if (UserUtil.isUserLogin()) {
                        // If logged in, check if they already have a package
                        if (UserUtil.getHasPackage() == "no") {
                            listener.onAgencyYearlyPlanClick(model) // Call your listener
                        } else {
                            Toast.makeText(itemView.context, R.string.you_already_have_a_package, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // If not logged in, navigate to the login dialog
                        navController.navigate(R.id.loginDialog, null, navOptionsAnimation())
                    }
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