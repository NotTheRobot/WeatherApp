package com.denis.weatherapp.network.model

import com.google.gson.annotations.SerializedName

data class WeekForecastResponse(
    @SerializedName("data")
    val data: List<WeekForecastDTO>
)
