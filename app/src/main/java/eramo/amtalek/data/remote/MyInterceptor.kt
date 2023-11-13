package eramo.amtalek.data.remote

import eramo.amtalek.util.API_HEADER_LANG_AR
import eramo.amtalek.util.API_HEADER_LANG_EN
import eramo.amtalek.util.LocalUtil
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("Accept", "application/json")
            .header("lang", if (LocalUtil.isEnglish()) API_HEADER_LANG_EN else API_HEADER_LANG_AR)
            .build()
        return chain.proceed(request)
    }
}