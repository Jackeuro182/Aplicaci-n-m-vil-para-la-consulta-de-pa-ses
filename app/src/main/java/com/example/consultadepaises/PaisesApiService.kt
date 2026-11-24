package com.example.consultadepaises

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface PaisesApiService {
    @GET("countries/v5")
    suspend fun obtenerPaises(): PaisesResponse
}
object RetrofitClient {
    private const val BASE_URL = "https://api.restcountries.com/"
    private const val BEARER_TOKEN = "rc_live_ee2d8e821ef6427abae0d9c5acd4ef54"

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $BEARER_TOKEN")
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val api: PaisesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PaisesApiService::class.java)
    }
}