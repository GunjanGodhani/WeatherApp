//package com.weatherinformation.network
//
//import com.weatherinformation.network.service.WeatherApiService
//import com.weatherinformation.network.urlfactory.UrlFactory
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
//class RetrofitProvider private constructor() {
//
//    val weatherApiService : WeatherApiService
//
//    init {
//        val httpLoggingInterceptor by lazy {
//            HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            }
//        }
//
//        val okHttpClient: OkHttpClient by lazy {
//            OkHttpClient.Builder()
//                .addInterceptor(httpLoggingInterceptor)
//                .connectTimeout(1, TimeUnit.MINUTES)
//                .writeTimeout(1, TimeUnit.MINUTES)
//                .readTimeout(1, TimeUnit.MINUTES)
//                .build()
//        }
//
//        val retrofit = Retrofit.Builder()
//            .client(okHttpClient)
//            .baseUrl(UrlFactory.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create()).build()
//        weatherApiService = retrofit.create(WeatherApiService::class.java)
//
//    }
//
//    companion object {
//        private var retrofitObj: RetrofitProvider? = null
//        fun provideRetrofitObject(): RetrofitProvider {
//            if (retrofitObj == null) {
//                retrofitObj = RetrofitProvider()
//            }
//            return retrofitObj!!
//        }
//    }
//
//}