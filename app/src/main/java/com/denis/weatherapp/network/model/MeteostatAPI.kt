package com.denis.weatherapp.network.model

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MeteostatAPI {

    @Headers(
        MeteostatAPICompanion.keyHeader,
        MeteostatAPICompanion.hostHeader
    )
    @GET("point/daily")
    suspend fun getForecast(@Query("lat") lat: Float, @Query("lon") lon: Float,
                    @Query("start") startDate: String, @Query("end") endDate: String): WeekForecastResponse

    @Headers(
        MeteostatAPICompanion.keyHeader,
        MeteostatAPICompanion.hostHeader
    )
    @GET("point/hourly")
    suspend fun getCurrentTemp(@Query("lat") lat: Float, @Query("lon") lon: Float,
                       @Query("start") startDate: String, @Query("end") endDate: String,
                               @Query("tz") timeZone: String): OneDayForecastResponse
}

class MeteostatAPICompanion{
    companion object{
        const val keyHeader = "X-RapidAPI-Key: e926ba6473msh56c9721fbb1012dp12cea1jsn9c1f58d96bc0"
        const val hostHeader = "X-RapidAPI-Host: meteostat.p.rapidapi.com"
    }
}
