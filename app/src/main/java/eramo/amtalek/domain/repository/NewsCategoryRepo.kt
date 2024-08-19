package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.myHome.news.newscateg.NewsCategoryResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface NewsCategoryRepo {
    suspend fun getAllNewsCategories(id: Int): Flow<Resource<NewsCategoryResponse>>
}