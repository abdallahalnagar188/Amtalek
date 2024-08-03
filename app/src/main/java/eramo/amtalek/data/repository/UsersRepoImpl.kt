package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.data.remote.dto.userDetials.UserDetailsResponse
import eramo.amtalek.domain.repository.BrokersRepo
import eramo.amtalek.domain.repository.UsersDetailsRepo


class UsersRepoImpl(private val apiService: AmtalekApi) :UsersDetailsRepo {
    override suspend fun getUsersDetailsFromRemote(id: Int): UserDetailsResponse {
        return apiService.getUserDetails(id)
    }
}