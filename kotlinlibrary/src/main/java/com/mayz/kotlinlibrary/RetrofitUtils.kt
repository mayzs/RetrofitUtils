package com.mayz.kotlinlibrary


import com.mayz.kotlinlibrary.builder.DeleteBuilder
import com.mayz.kotlinlibrary.builder.GetBuilder
import com.mayz.kotlinlibrary.builder.PostBuilder
import com.mayz.kotlinlibrary.builder.PutBuilder
import com.mayz.kotlinlibrary.https.HttpsUtils
import okhttp3.Interceptor
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * @createDate: 2020/5/8
 * @author:     mayz
 * @version:    1.0
 */
object RetrofitUtils {
    private var sslParams: HttpsUtils.SSLParams? = null
    private var interceptorsList: List<Interceptor> = mutableListOf()
    private var networkInterceptor: Interceptor? = null

    fun get(): GetBuilder {
        return GetBuilder()
    }
    fun post(): PostBuilder {
        return PostBuilder(Params.POST)
    }

    /**
     * 上传多个文件
     *
     * @param fileMethodName
     * @param files
     * @return
     */
    fun post(
        fileMethodName: String,
        files: List<File>
    ): PostBuilder {
        return PostBuilder(Params.POST_FILE, fileMethodName, files)
    }

    /**
     * 上传单个文件
     *
     * @param fileMethodName
     * @param file
     * @return
     */
    fun post(fileMethodName: String?, file: File?): PostBuilder? {
        return PostBuilder(Params.POST_FILE, fileMethodName, file)
    }

    /**
     * 传json
     *
     * @param jsonObject
     * @return
     */
    fun post(jsonObject: JSONObject?): PostBuilder? {
        return PostBuilder(Params.POST_JSON, jsonObject!!)
    }

    /**
     * 传json
     *
     * @param jsonParams
     * @return
     */
    fun post(jsonParams: Map<String, String>): PostBuilder {
        return PostBuilder(Params.POST_JSON, jsonParams!!)
    }

    fun delete(): DeleteBuilder {
        return DeleteBuilder()
    }

    fun put(): PutBuilder {
        return PutBuilder()
    }

    fun setSslParams(sslParams: HttpsUtils.SSLParams?): RetrofitUtils? {
        this.sslParams = sslParams
        return this
    }

    fun addInterceptor(interceptor: Interceptor?): RetrofitUtils? {
        if (interceptor != null) {
            interceptorsList.plus(interceptor)
        }
        return this
    }

    fun addNetworkInterceptor(interceptor: Interceptor?): RetrofitUtils? {
        networkInterceptor = interceptor
        return this
    }

    fun getSslParams(): HttpsUtils.SSLParams? {
        return if (sslParams != null) {
            sslParams
        } else {

            HttpsUtils.getSslSocketFactory()
        }
    }

    fun getInterceptorsList(): List<Interceptor?>? {
        return interceptorsList
    }

    fun getNetworkInterceptor(): Interceptor? {
        return if (networkInterceptor != null) networkInterceptor else null
    }
}