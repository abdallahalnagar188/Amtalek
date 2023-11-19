package eramo.amtalek.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.model.request.OrderExtraList
import eramo.amtalek.domain.model.request.OrderItemList
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    val openDrawer = MutableLiveData<Boolean>()

    //    val profileData = MutableLiveData<Member>()
    val cartCount = MutableLiveData<Int>()

    val profileData = MutableStateFlow<UiState<UserModel>>(UiState.Empty())

    val dateString = MutableLiveData<String?>(null)

    //____________________________________________________________________________________________//
    // order

    var orderItemList = ArrayList<OrderItemList>()
    var orderExtraList = ArrayList<OrderExtraList>()
    var paymentType = ""
    var orderPromoCode = 0

    fun getOrderRequestInstance(): OrderRequest {
        return OrderRequest(
            userId = UserUtil.getUserId(),
            userName = UserUtil.getUserFirstName(),
            userPhone = UserUtil.getUserPhone(),
            payType = paymentType,
            token = UserUtil.getUserToken(),
            promoCode = orderPromoCode,
            orderItemList = orderItemList,
            orderExtraList = orderExtraList,
        )
    }

    fun resetOrder() {
        orderExtraList.clear()
        orderItemList.clear()
        paymentType = ""
        orderPromoCode = 0
    }

}