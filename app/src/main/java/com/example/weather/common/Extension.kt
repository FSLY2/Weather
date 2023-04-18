package com.example.weather.common

import com.example.weather.R
import java.text.SimpleDateFormat
import java.util.*

const val HOUR_DOT_MINUTE = "HH:mm"

fun String.provideIcon() = when(this) {
    "113" -> R.drawable.ic_113d
    "116" -> R.drawable.ic_116d
    "119", "122" -> R.drawable.ic_119d
    "143", "248", "260", "185", "311", "314", "281", "284" -> R.drawable.ic_143d
    "176", "293", "299", "353" -> R.drawable.ic_176d
    "179", "323", "329", "335", "362", "365", "368", "371", "374", "377" -> R.drawable.ic_179d
    "200", "386", "392" -> R.drawable.ic_200d
    "263", "266", "296", "302", "317", "320" -> R.drawable.ic_263d
    "305", "356", "359" -> R.drawable.ic_305d
    "308" -> R.drawable.ic_308d
    "389" -> R.drawable.ic_389d
    "395" -> R.drawable.ic_395d
    else -> R.drawable.ic_error
}

fun Int.toDateFormat(format: String): String {
    val cal = Calendar.getInstance()
    val timeZone = cal.timeZone
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(Date(this.toLong() * 1000))
}