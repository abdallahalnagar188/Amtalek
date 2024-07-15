package eramo.amtalek.domain.search

import android.os.Parcelable
import eramo.amtalek.domain.model.property.CriteriaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchDataListsModel(
    val listOfPurposeItems:List<CriteriaModel>,
    val listOfFinishingItems:List<CriteriaModel>,
    val listOfTypesItems:List<CriteriaModel>,
    val listOfCurrencyItems:List<CriteriaModel>
) : Parcelable
