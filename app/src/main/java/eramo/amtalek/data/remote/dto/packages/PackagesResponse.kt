package eramo.amtalek.data.remote.dto.packages


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.PackageModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PackagesResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable
@Parcelize
data class Data(
    @SerializedName("accounting_module")
    var accountingModule: String?,
    @SerializedName("crm_agents")
    var crmAgents: String?,
    @SerializedName("emoney")
    var emoney: String?,
    @SerializedName("featured")
    var featured: String?,
    @SerializedName("featured_listings")
    var featuredListings: String?,
    @SerializedName("featured_package_text")
    var featuredPackageText: String?,
    @SerializedName("hr_module")
    var hrModule: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("leads_management")
    var leadsManagement: String?,
    @SerializedName("messages")
    var messages: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("normal_listings")
    var normalListings: String?,
    @SerializedName("package_type")
    var packageType: String?,
    @SerializedName("price_monthly")
    var priceMonthly: String?,
    @SerializedName("price_yearly")
    var priceYearly: String?,
    @SerializedName("projects")
    var projects: String?,
    @SerializedName("sub_title")
    var subTitle: String?,
    @SerializedName("supervisors")
    var supervisors: String?
) : Parcelable{
    fun toPackageModel(): PackageModel {
        return PackageModel(
            id = id?:-1,
            name = name?:"",
            priceMonthly = priceMonthly?:"",
            priceYearly = priceYearly?:"",
            packageType = packageType?:"",
            featuredPackageText = featuredPackageText?:"",
            normalListings = normalListings?:"",
            messages = messages?:"",
            leadsManagement = leadsManagement?:"",
            projects = projects?:"",
            hrModule = hrModule?:"",
            accountingModule = accountingModule?:"",
            emoney = emoney?:"",
            featuredListings = featuredListings?:"",
            supervisors = supervisors?:"",
            crmAgents = crmAgents?:"",
            subTitle = subTitle?:""
        )
    }

}