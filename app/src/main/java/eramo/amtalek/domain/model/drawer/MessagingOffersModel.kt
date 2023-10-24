package eramo.amtalek.domain.model.drawer

data class MessagingOffersModel(
    val senderImageUrl: String,
    val senderStatus: Int,
    val senderName: String,
    val lastMessage: String,
    val date: String,
    val unseenCount: Int,

    val offerImageUrl: String,
    val offerTitle: String,
    val offerPrice: Int
)
