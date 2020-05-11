package com.mayz.kotlinlibrary.builder

import android.text.TextUtils
import com.mayz.kotlinlibrary.Params
import com.mayz.kotlinlibrary.apiservice.APIService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * @createDate: 2020/5/8
 * @author:     mayz
 * @version:    1.0
 */
class PostBuilder : BaseBuilder {
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private var jsonObject: JSONObject? = null
    private var jsonParams: Map<String, String>? = null
    private var fileMethodName: String? = null
    private var file: File? = null
    private var files: List<File>? = null

    constructor(params: Params) : super(params){
        this.params=params
    }
    constructor(params: Params, jsonObject: JSONObject) : super(params){
        this.params=params
        this.jsonObject=jsonObject
    }
    constructor(params: Params, jsonParams: Map<String,String>) : super(params){
        this.params=params
        this.jsonParams=jsonParams
    }

    constructor(params: Params, fileMethodName: String?, file: File?):super(params) {

        this.params = params
        this.fileMethodName = fileMethodName
        this.file = file
    }

    constructor(params: Params, fileMethodName: String?, files: List<File>?):super(params) {

        this.params = params
        this.fileMethodName = fileMethodName
        this.files = files
    }

    override fun getObservable(
            apiService: APIService?,
            headMaps: Map<String?, String?>?,
            url: String,
            paramMap: Map<String?, String?>?
    ): Observable<String> {
        var observable: Observable<String>
        if (params === Params.POST_JSON) {
            var json = ""
            if (jsonParams!!.isNotEmpty()) {
                jsonObject = JSONObject()
                for (key in jsonParams!!.keys) {
                    try {
                        jsonObject!!.put(key, jsonParams!![key!!])
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            val requestBody = RequestBody.create(JSON, json)
            observable = apiService!!.postJson(headMaps!!, url!!, requestBody!!)
            return observable
        } else if (params === Params.POST_FILE) {
            val parts: MutableList<MultipartBody.Part> =
                ArrayList()
            if (!TextUtils.isEmpty(fileMethodName)) {
                if (files!!.isNotEmpty()) {
                    for (listFile in files!!) {
                        val requestBody = RequestBody.create(
                            MediaType.parse("multipart/form-data"),
                            listFile
                        )
                        val part = MultipartBody.Part.createFormData(
                            fileMethodName,
                            listFile.name,
                            requestBody
                        )
                        parts.add(part)
                    }
                } else if (file != null) {
                    val requestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val part = MultipartBody.Part.createFormData(
                        fileMethodName,
                        file!!.name,
                        requestBody
                    )
                    parts.add(part)
                }
            }
            observable = apiService!!.postFile(headMaps!!, url!!, parts!!)
            return observable
        }
        return super.getObservable(apiService, headMaps, url, paramMap)
    }
}