package eramo.amtalek.domain.usecase.user

import eramo.amtalek.domain.repository.UsersDetailsRepo
import javax.inject.Inject

class GetUserDetails @Inject constructor(private val usersDetailsRepo: UsersDetailsRepo) {
    suspend operator fun invoke(id:Int) = usersDetailsRepo.getUsersDetailsFromRemote(id)

}