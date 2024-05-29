package eramo.amtalek.domain.model.drawer

import com.google.gson.annotations.SerializedName

data class PackageModel(
    val accountingModule: String,
    val crmAgents: String,
    val emoney: String,
    val featuredListings: String,
    val featuredPackageText: String,
    val hrModule: String,
    val id: Int,
    val leadsManagement: String,
    val messages: String,
    val name: String,
    val normalListings: String,
    val packageType: String,
    val priceMonthly: String,
    val priceYearly: String,
    val projects: String,
    val subTitle: String,
    val supervisors: String
    )
