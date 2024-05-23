package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.extra_sections.HomeExtraSectionsResponse
import eramo.amtalek.data.remote.dto.myHome.featured_properety.HomeFeaturedPropertiesResponse
import eramo.amtalek.data.remote.dto.myHome.filter_by_city.HomeCitiesResponse
import eramo.amtalek.data.remote.dto.myHome.mostviewd.HomeMostViewsResponse
import eramo.amtalek.data.remote.dto.myHome.news.HomeNewsResponse
import eramo.amtalek.data.remote.dto.myHome.normal.HomeNormalPropertiesResponse
import eramo.amtalek.data.remote.dto.myHome.project.HomeProjectsResponse
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.domain.repository.MyHomeRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyHomeRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):MyHomeRepository {
    override suspend fun getHomeFeaturedProperty(countryId: String): Flow<Resource<HomeFeaturedPropertiesResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeFeaturedProperty(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null , countryId = countryId)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }

    }
    override suspend fun getHomeProjects(countryId: String):Flow<Resource<HomeProjectsResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeProjects(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null , countryId = countryId)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }
    }
    override suspend fun getFilterByCity(countryId: String):Flow<Resource<HomeCitiesResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getFilterByCity(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null , countryId = countryId)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }
    }
    override suspend fun getHomeSlider():Flow<Resource<HomeSlidersResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeSlider(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        } }
    override suspend fun getHomeMostViewedProperties(countryId: String):Flow<Resource<HomeMostViewsResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeMostViewedProperties(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null, countryId = countryId)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }
    }

    override suspend fun getHomeNormalProperties(countryId: String): Flow<Resource<HomeNormalPropertiesResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeNormalProperties(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null, countryId = countryId)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }
    }

    override suspend fun getHomeNews():Flow<Resource<HomeNewsResponse>> {
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeNews(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }

    }
    override suspend fun getHomeExtraSections(countryId: String):Flow<Resource<HomeExtraSectionsResponse>>{
        return flow {
            val result  = toResultFlow { amtalekApi.getHomeNewestSections(userToken = if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,countryId = countryId)}
            result.collect(){
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }

            }
        }

    }
}