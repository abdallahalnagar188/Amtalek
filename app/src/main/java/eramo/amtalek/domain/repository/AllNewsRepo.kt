package eramo.amtalek.domain.repository

import androidx.paging.PagingSource
import eramo.amtalek.data.remote.dto.myHome.news.allnews.AllNewsResponse
import eramo.amtalek.data.remote.dto.myHome.news.allnews.DataX
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface AllNewsRepo {
    suspend fun getAllNews(): Flow<Resource<AllNewsResponse>>
    fun getAllNewsPagingSource(): PagingSource<Int, DataX>
}