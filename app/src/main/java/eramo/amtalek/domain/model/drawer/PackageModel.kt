package eramo.amtalek.domain.model.drawer

data class PackageModel(
    val title: String,
    val description: String,
    val price: Int,
    val normal: Int,
    val featured: Int,
    val promotion: Int,
    val leads:Int,
    val extraLeadsPrice:Int,
    val coverColor:String,
    val priceColor:String,
)
