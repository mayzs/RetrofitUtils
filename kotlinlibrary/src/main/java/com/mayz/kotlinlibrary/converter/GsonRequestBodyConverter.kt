package com.mayz.kotlinlibrary.converter

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
class GsonRequestBodyConverter<T>: Converter<T, RequestBody> {
    val MEDIA_TYPE=MediaType.parse("application/json; charset=UTF-8")
    override fun convert(value: T): RequestBody? {
        return RequestBody.create(MEDIA_TYPE,value.toString())
    }
}