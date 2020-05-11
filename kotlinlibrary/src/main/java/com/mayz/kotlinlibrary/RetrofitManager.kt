package com.mayz.kotlinlibrary

import com.mayz.kotlinlibrary.apiservice.APIService
import com.mayz.kotlinlibrary.converter.GsonConverFactory
import com.mayz.kotlinlibrary.https.HttpsUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
object RetrofitManager{
    private var BASEURL:String=""
    private var mRetrofit:Retrofit? = null
    //var sRetrofitManager: SparseArray<RetrofitManager> = SparseArray<RetrofitManager>()
    //var mAPIService:APIService?=null
    private var sOkHttpClient:OkHttpClient?=null
    fun getAPIService(): APIService? {
        return mRetrofit?.create(APIService::class.java)
    }

    fun setUrl(url:String):RetrofitManager{
        BASEURL = url
        mRetrofit = Retrofit.Builder().baseUrl(BASEURL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        return this
    }
    fun getOkHttpClient():OkHttpClient?{
        if (sOkHttpClient == null) {
            synchronized(RetrofitManager::class.java) {
                //                Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(), "HttpCache"),
                //                        1024 * 1024 * 100);
                if (sOkHttpClient == null) {
                    val builder =
                        OkHttpClient.Builder() //                            .cache(cache)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                    //.addInterceptor(mRewriteCacheControlInterceptor);
                    //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
                    //.addInterceptor(mLoggingInterceptor);
                    val sslParams: HttpsUtils.SSLParams? = RetrofitUtils.getSslParams()
                    if (sslParams != null) {
                        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    }
                    val interceptorsList: List<Interceptor> =
                        RetrofitUtils.getInterceptorsList() as List<Interceptor>
                    if (interceptorsList != null && interceptorsList.size > 0) {
                        for (interceptor in interceptorsList) {
                            builder.addInterceptor(interceptor)
                        }
                    }
                    val networkInterceptor: Interceptor? =
                        RetrofitUtils.getNetworkInterceptor()
                    if (networkInterceptor != null) {
                        builder.addNetworkInterceptor(networkInterceptor)
                    }
                    sOkHttpClient = builder.build()
                }
            }
        }
        return sOkHttpClient
    }
}