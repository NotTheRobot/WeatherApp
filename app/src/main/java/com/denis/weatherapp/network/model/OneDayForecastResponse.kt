package com.denis.weatherapp.network.model

import com.google.gson.annotations.SerializedName

data class OneDayForecastResponse(
    @SerializedName("data")
    val data: List<OneDayForecastDTO>
)
