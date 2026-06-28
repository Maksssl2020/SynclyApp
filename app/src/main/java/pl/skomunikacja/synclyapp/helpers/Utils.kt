package pl.skomunikacja.synclyapp.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import androidx.core.graphics.toColorInt

object Utils {

    fun isBase64Image(value: String): Boolean {
        val trimmed = value.trim()

        return trimmed.startsWith("/9j/") || // JPEG
                trimmed.startsWith("iVBOR") || // PNG
                trimmed.startsWith("R0lGOD") || // GIF
                trimmed.startsWith("data:image")
    }

    fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            val bitmap = retriever.getFrameAtTime(0)
            retriever.release()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun formatIsoDate(dateString: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm",
                Locale("en", "EN")
            )

            val date = LocalDateTime.parse(dateString, inputFormatter)
            date.format(outputFormatter)
        } catch (e: Exception) {
            dateString
        }
    }

    @Composable
    fun rememberBase64Image(imageBase64: String?): ImageBitmap? {
        return remember(imageBase64) {
            if (imageBase64.isNullOrEmpty()) return@remember null

            try {
                val pureBase64 = imageBase64.substringAfter(",")
                val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
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
            days > 0 -> "$days days ago"
            hours > 0 -> "$hours hours ago"
            minutes > 0 -> "$minutes minutes ago"
            else -> "a moment ago"
        }
    }

}

fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val cleanBase64 = base64String
            .substringAfter("base64,", base64String)
            .trim()

        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun isDirectVideoUrl(url: String): Boolean {
    val cleanUrl = url.substringBefore("?").lowercase()

    return cleanUrl.endsWith(".mp4") ||
            cleanUrl.endsWith(".webm") ||
            cleanUrl.endsWith(".m3u8") ||
            cleanUrl.endsWith(".mkv") ||
            cleanUrl.endsWith(".avi")
}

fun isYoutubeUrl(url: String): Boolean {
    return url.contains("youtube.com") || url.contains("youtu.be")
}

fun normalizeWebsiteUrl(url: String): String {
    val trimmedUrl = url.trim()

    return if (
        trimmedUrl.startsWith("http://") ||
        trimmedUrl.startsWith("https://")
    ) {
        trimmedUrl
    } else {
        "https://$trimmedUrl"
    }
}

fun parseHexColor(hex: String): Color =
    try {
        Color(hex.toColorInt())
    } catch (e: IllegalArgumentException) {
        Teal100
    }
