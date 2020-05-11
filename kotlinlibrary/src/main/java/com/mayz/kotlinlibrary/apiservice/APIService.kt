package com.mayz.kotlinlibrary.apiservice

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
interface APIService {
    @GET()
    fun get(@HeaderMap headerMap: Map<String?, String?>, @Url url:String, @QueryMap params: Map<String?, String?>):Observable<String>

    @FormUrlEncoded
    @POST()
    fun post(@HeaderMap headerMap: Map<String?, String?>, @Url url: String,@FieldMap params: Map<String?, String?>):Observable<String>

    @POST()
    fun postJson(@HeaderMap headerMap: Map<String?, String?>, @Url url: String,@Body requestBody: RequestBody):Observable<String>

    @Multipart
    @POST()
    fun postFile(@HeaderMap headerMap: Map<String?, String?>, @Url url: String,@Part() parts:List<MultipartBody.Part>):Observable<String>

    @DELETE()
    fun delete(@HeaderMap headerMap: Map<String?, String?>, @Url url: String,@QueryMap params: Map<String?, String?>):Observable<String>

    @PUT()
    fun put(@HeaderMap headerMap: Map<String?, String?>, @Url url: String,@QueryMap params: Map<String?, String?>):Observable<String>

    @Streaming
    @GET()
    fun download(@Url url: String):Observable<ResponseBody>
}