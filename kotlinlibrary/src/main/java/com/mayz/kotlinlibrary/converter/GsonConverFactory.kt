package com.mayz.kotlinlibrary.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
object GsonConverFactory: Converter.Factory() {
    fun create():GsonConverFactory{
        return GsonConverFactory
    }
    override fun responseBodyConverter( type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return GsonResponseBodyConverter<Type>(type)
        //return super.responseBodyConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return GsonRequestBodyConverter<Type>()
        //return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }
}