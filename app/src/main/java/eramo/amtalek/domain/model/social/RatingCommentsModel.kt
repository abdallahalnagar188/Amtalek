package eramo.amtalek.domain.model.social

data class RatingCommentsModel(
    val userName: String,
    val userId: String,
    val userImageUrl: String,
    val comment: String,
    val date: String,
    val rate: Float,
)
