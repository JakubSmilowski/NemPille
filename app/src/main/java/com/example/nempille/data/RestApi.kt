
package com.example.nempille.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {
    @GET("notify")
    suspend fun notifyDevice(
        @Query("med") med: String,
        @Query("note") note: String,
        @Query("motor") motor: Int
    ): Response<Unit>
}

object RetrofitInstance {
    private const val BASE_URL = "http://172.20.10.2/"

    val api: RestApi by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(RestApi::class.java)
    }
}
