package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.data.remote.dto.userDetials.UserDetailsResponse

interface UsersDetailsRepo {
    suspend fun getUsersDetailsFromRemote(id:Int): UserDetailsResponse
}