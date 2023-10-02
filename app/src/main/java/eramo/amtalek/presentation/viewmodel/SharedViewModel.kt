package eramo.amtalek.presentation.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.domain.model.PreviewDetails
import eramo.amtalek.domain.model.PreviewRequest
import eramo.amtalek.domain.model.request.OrderExtraList
import eramo.amtalek.domain.model.request.OrderItemList
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.util.UserUtil
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    val openDrawer = MutableLiveData<Boolean>()
    val profileData = MutableLiveData<Member>()
    val cartCount = MutableLiveData<Int>()

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
            userName = UserUtil.getUserName(),
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