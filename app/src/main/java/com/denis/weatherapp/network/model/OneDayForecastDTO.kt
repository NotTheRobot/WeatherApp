package com.denis.weatherapp.network.model

import com.google.gson.annotations.SerializedName

data class OneDayForecastDTO(
    @SerializedName("temp")
    val avgTemp: Float,
)
