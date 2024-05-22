package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.myHome.featured_properety.HomeFeaturedPropertiesResponse
import eramo.amtalek.data.remote.dto.myHome.filter_by_city.HomeCitiesResponse
import eramo.amtalek.data.remote.dto.myHome.project.HomeProjectsResponse
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface MyHomeRepository {
    suspend fun getHomeFeaturedProperty(countryId: String):Flow<Resource<HomeFeaturedPropertiesResponse>>
    suspend fun getHomeProjects(countryId: String):Flow<Resource<HomeProjectsResponse>>
    suspend fun getFilterByCity(countryId: String):Flow<Resource<HomeCitiesResponse>>
    suspend fun getHomeSlider():Flow<Resource<HomeSlidersResponse>>
    suspend fun getHomeMostViewedProperties(countryId: String)
    suspend fun getHomeNews(countryId: String)

}