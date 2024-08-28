package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.packages.PackagesResponse
import eramo.amtalek.data.remote.dto.packages.SubscribeToPackageResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PackagesRepository {
    suspend fun getPackages(type: String): Flow<Resource<PackagesResponse>>
    suspend fun subscribeToPackage(duration: String, actorType: String, packageId: String): Flow<Resource<SubscribeToPackageResponse>>
}