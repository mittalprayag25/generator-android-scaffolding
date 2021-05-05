package <%= appPackage %>.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import <%= appPackage %>.commons.config.Config
import <%= appPackage %>.retrofit.services.SuperAppService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SuperAppServiceApi : SuperAppService {
    override fun api(id: String, orderId: String): Observable<Response<String>> {
        return superAppService
                .api("11", "23")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun apiNext(id: String): Observable<Response<String>> {
        return superAppService
            .api("11", "23")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    lateinit var superAppService: SuperAppService
    lateinit var retrofit: Retrofit

    constructor(token: String) {
        val httpClient = OkHttpClient.Builder()
        val authorizationInterceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .header("Authorization", token)
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build()

            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(loggingInterceptor)

        httpClient.addInterceptor(authorizationInterceptor)
        httpClient.readTimeout(Config.SERVER_READ_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(Config.SERVER_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Config.SERVER_CONNECT_TIMEOUT,TimeUnit.SECONDS)
        val okHttpClient = httpClient.build()
        initRetrofit(okHttpClient)
    }

    constructor() {
        val httpClient = OkHttpClient.Builder()

        val authorizationInterceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build()

            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(loggingInterceptor)

        httpClient.addInterceptor(authorizationInterceptor)
        httpClient.readTimeout(Config.SERVER_READ_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(Config.SERVER_WRITE_TIMEOUT,TimeUnit.SECONDS)
                .connectTimeout(Config.SERVER_CONNECT_TIMEOUT,TimeUnit.SECONDS)
        val okHttpClient = httpClient.build()
        initRetrofit(okHttpClient)
    }

    private fun initRetrofit(okHttpClient: OkHttpClient) {
        retrofit = Retrofit.Builder()
                .baseUrl(Config.SERVER_ENVIRONMENT.getServerUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        superAppService = retrofit.create(SuperAppService::class.java)
    }
}