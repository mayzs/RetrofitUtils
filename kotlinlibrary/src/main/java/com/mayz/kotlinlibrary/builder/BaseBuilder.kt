package com.mayz.kotlinlibrary.builder


import com.google.gson.Gson
import com.mayz.kotlinlibrary.Params
import com.mayz.kotlinlibrary.RetrofitManager
import com.mayz.kotlinlibrary.apiservice.APIService
import com.mayz.kotlinlibrary.delagate.Callback
import com.mayz.kotlinlibrary.delagate.DataCallback
import com.mayz.kotlinlibrary.delagate.FileCallback
import com.mayz.kotlinlibrary.delagate.StringCallback
import com.mayz.kotlinlibrary.schedulers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
 open class BaseBuilder(var params: Params) {
    private var baseUrl = ""
    private var url = ""

    //private var headMap: Map<String?, String?> = TreeMap()
    private val headMap = mutableMapOf<String?, String?>()
    private val paramsMap = mutableMapOf<String?, String?>()
    fun url(url: String): BaseBuilder? {
        var index: Int = url?.indexOf("/") ?: 0
        index = url.indexOf("/", index + 2)
        this.baseUrl = url.substring(0, index + 1)
        this.url = url
        return this
    }

    fun header(
        key: String,
        value: String
    ): BaseBuilder {
        headMap.put(key, value)
        return this
    }

    fun headers(
        headMaps: Map<String?, String?>?
    ): BaseBuilder? {
        //if (headMaps != null) {
        headMap.putAll(headMaps!!)
        //}
        return this
    }

    fun param(
        key: String,
        value: String
    ): BaseBuilder {
        paramsMap.put(key, value)
        return this
    }

    fun params(
        paramMap: MutableMap<String, String>
    ): BaseBuilder {
        paramsMap.putAll(paramMap!!)
        return this
    }

    fun execute(callback: Callback){
        if (url.isNotEmpty()&&baseUrl.isNotEmpty()){
            var apiService: APIService?= RetrofitManager.setUrl(baseUrl).getAPIService()
            var observable:Observable<String>?=null
            if (params==Params.GET){
                observable=apiService!!.get(headMap,url,paramsMap)
            } else if (params === Params.POST) {
                observable = apiService!!.post(headMap, url, paramsMap)
            } else if (params === Params.POST_JSON || params === Params.POST_FILE) {
                observable = getObservable(apiService!!, headMap, url, paramsMap)
            } else if (params === Params.DELETE) {
                observable = apiService!!.delete(headMap, url, paramsMap)
            } else if (params === Params.PUT) {
                observable = apiService!!.put(headMap, url, paramsMap)
            }
            if (callback!! is StringCallback){
                execute(observable,callback as StringCallback)
            }else if (callback!! is DataCallback<*>){
              execute(observable,callback as DataCallback<*>)
            }

        }else{
            callback!!.onError("请检查url是否正确!")
        }
    }

    private fun execute(observable: Observable<String>?, callback: StringCallback) {
        observable!!.subscribeOn(SchedulerProvider.io())
            .observeOn(SchedulerProvider.ui())
            .subscribe(object : Observer<String> {
                //订阅
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    e.printStackTrace() //请求过程中发生错误
                    callback!!.onError(e.message)
                }

                override fun onNext(s: String) { //这里的book就是我们请求接口返回的实体类
                    callback!!.onSuccess(s)
                }
            })
    }

    private fun <T> execute(observable: Observable<String>?, callback: DataCallback<T>) {
        observable!!.subscribeOn(SchedulerProvider.io())
            .observeOn(SchedulerProvider.ui())
            .subscribe(object : Observer<String> {
                //订阅
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    e.printStackTrace() //请求过程中发生错误
                    callback!!.onError(e.message)
                }

                override fun onNext(s: String) { //这里的book就是我们请求接口返回的实体类
                    val a: Array<Type> = callback::class.java.genericInterfaces
                    if (a!!.isNotEmpty()) {
                        if ("java.lang.Class" == a[0].javaClass.name) {
                            return
                        }
                        val temp = (a[0] as ParameterizedType).actualTypeArguments
                        if (temp!!.isNotEmpty()) {
                            var t:T= Gson().fromJson(s,temp[0])
                            //var t: T = JSON.parseObject(s, temp[0])
                            callback.onSuccess(t)
                        }
                    }
                }
            })
    }
    protected open fun getObservable(
        apiService: APIService?,
        headMaps: Map<String?, String?>?,
        url: String,
        paramMap: Map<String?, String?>?
    ): Observable<String> {
        TODO("Not yet implemented")
    }

    fun execute(fileCallback: FileCallback) {
        var range: Long = 0
        var apiService: APIService? = RetrofitManager.setUrl(baseUrl).getAPIService()
        var observable: Observable<ResponseBody> = apiService!!.download(url)
        observable!!.subscribeOn(SchedulerProvider.io())
            .observeOn(SchedulerProvider.io())
            .subscribe(object :Observer<ResponseBody>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(responseBody: ResponseBody) {

                    GlobalScope.launch {
                        fileCallback!!.saveFile(range,responseBody)
                    }
                }

                override fun onError(e: Throwable) {

                    fileCallback!!.onError(e.message)
                }

            })
    }
}

