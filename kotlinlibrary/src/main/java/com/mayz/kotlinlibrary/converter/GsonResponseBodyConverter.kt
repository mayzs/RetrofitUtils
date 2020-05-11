package com.mayz.kotlinlibrary.converter

import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
class GsonResponseBodyConverter<T>(var type: Type) :Converter<ResponseBody,T> {

    override fun convert(value: ResponseBody): T? {
//        var clazz:Class<*>  = type as Class<*>
//        var canoicalName:String? = clazz.canonicalName
//        if (canoicalName?.equals(JSONObject::class.java.name)!!){
//            var jsonObj:JSONObject= JSONObject(value.string())
//            return jsonObj as T
//        }else if (canoicalName?.equals(JSONArray::class.java.name)!!){
//            var jsonArr:JSONArray = JSONArray(value.string())
//            return jsonArr as T
//        }else{
//            var date:T=Gson().fromJson(value.string(),this.type)
//            return date
//        }
        return value.string() as T
    }
}