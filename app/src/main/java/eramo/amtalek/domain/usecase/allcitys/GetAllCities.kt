package eramo.amtalek.domain.usecase.allcitys

import eramo.amtalek.data.remote.dto.myHome.allCitys.AllCityResponse
import eramo.amtalek.domain.repository.AllCitiesRepo
import eramo.amtalek.domain.repository.AllPropertyRepo
import javax.inject.Inject

class GetAllCities @Inject constructor(private val allCityRepo: AllCitiesRepo) {
    suspend operator fun invoke() = allCityRepo.getAllCitiesFromRemote()
}