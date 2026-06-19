package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import pl.skomunikacja.synclyapp.helpers.Utils.isBase64Image
import pl.skomunikacja.synclyapp.helpers.base64ToBitmap
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400


@Composable
fun PostImage(
    imageValue: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (imageValue.isNullOrBlank()) {
        Box(
            modifier = modifier.background(Black300),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No photo",
                color = Gray400
            )
        }
        return
    }

    if (imageValue.startsWith("http://") || imageValue.startsWith("https://")) {
        AsyncImage(
            model = imageValue,
            contentDescription = "Post Image",
            modifier = modifier,
            contentScale = contentScale
        )
    } else if (isBase64Image(imageValue)) {
        val bitmap = remember(imageValue) {
            base64ToBitmap(imageValue)
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Post Image",
                modifier = modifier,
                contentScale = contentScale
            )
        } else {
            Box(
                modifier = modifier.background(Black300),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "The photo cannot be displayed",
                    color = Gray400
                )
            }
        }
    }
}