package com.github.asmaaatya.aqimsalat.api


import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object PrayerApiClient {
    //./gradlew runIde
    private val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    val service: PrayerTimesApi = retrofit.create(PrayerTimesApi::class.java)
}
