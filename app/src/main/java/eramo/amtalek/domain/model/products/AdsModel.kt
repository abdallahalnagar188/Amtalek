package eramo.amtalek.domain.model.products

data class AdsModel(
    var adsId: String,
    var adsIn: String,
    var clientType: String,
    var advName: String,
    var adsLocation: String,
    var advDate: String,
    var advTime: String,
    var spaceType: String,
    var clientIdFk: String,
    var type: String,
    var iframeText: String,
    var image: String,
    var numViews: String,
    var numClick: String,
    var fromDate: String,
    var fromDateS: String,
    var toDate: String,
    var toDateS: String,
    var status: String,
    var userId: String,
    var agentBrokFk: String,
    var parentUser: String,
    var actionDate: String,
    var actionTime: String,
    var actionUser: String,
    var actionView: String,
    var adsUrl: String,
    var mainTitle: String,
    var shortTitle: String,
)