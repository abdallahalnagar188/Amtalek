package eramo.amtalek.domain.model.drawer

data class MessagingChatModel (
    val senderImageUrl:String,
    val senderStatus:Int,
    val senderName:String,
    val lastMessage:String,
    val date:String,
    val unseenCount:Int,
)