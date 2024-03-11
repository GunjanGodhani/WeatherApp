package com.weatherinformation.utils

fun roundToTwoDecimalPlaces(value: Double): Double {
    return "%,.2f".format(value).toDouble()
}