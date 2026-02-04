package pl.skomunikacja.synclyapp.ui

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun rememberBase64Image(imageBase64: String?): ImageBitmap? {
    return remember(imageBase64) {
        if (imageBase64.isNullOrEmpty()) return@remember null

        try {
            val pureBase64 = imageBase64.substringAfter(",") // Usuwa prefix np. "data:image/png;base64,"
            val decodedBytes = android.util.Base64.decode(pureBase64, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun timeAgoOrDate(isoDate: String): String {
    val postInstant = try {
        Instant.parse(isoDate)
    } catch (e: Exception) {
        LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME)
            .atZone(ZoneId.systemDefault())
            .toInstant()
    }

    val nowInstant = Instant.now()
    val days = ChronoUnit.DAYS.between(postInstant, nowInstant)
    val hours = ChronoUnit.HOURS.between(postInstant, nowInstant)
    val minutes = ChronoUnit.MINUTES.between(postInstant, nowInstant)

    return when {
        days > 7 -> {
            val dateTime = LocalDateTime.ofInstant(postInstant, ZoneId.systemDefault())
            dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
        days > 0 -> "$days dni temu"
        hours > 0 -> "$hours godzin temu"
        minutes > 0 -> "$minutes minut temu"
        else -> "przed chwilą"
    }
}