package <%= appPackage %>.retrofit.services

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * REST API access points
 */
interface SuperAppService {
    @POST("URL")
    @Headers("Content-Type: application/json")
    fun api(@Path("id") id: String, @Path("orderId") orderId: String): Observable<Response<String>>

    @POST("URL1")
    @Headers("Content-Type: application/json")
    fun apiNext(@Path("id") id: String): Observable<Response<String>>

}
