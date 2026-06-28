package pl.skomunikacja.synclyapp.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times
import compose.icons.fontawesomeicons.solid.Video
import pl.skomunikacja.synclyapp.helpers.Utils.getVideoThumbnail
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100


@Composable
fun MediaPreviewGrid(
    mediaUris: List<Uri>,
    onRemoveMedia: (Uri) -> Unit,
    mediaType: String
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(mediaUris) { uri ->
            MediaPreviewItem(
                uri = uri,
                onRemove = { onRemoveMedia(uri) },
                mediaType = mediaType
            )
        }
    }
}

@Composable
fun MediaPreviewItem(
    uri: Uri,
    onRemove: () -> Unit,
    mediaType: String
) {
    val context = LocalContext.current
    val thumbnail by remember(uri) {
        mutableStateOf(
            if (mediaType == "video") getVideoThumbnail(context, uri) else null
        )
    }

    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Black200)
            .border(1.dp, Gray500, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (mediaType) {
                "photo" -> {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                "video" -> {
                    if (thumbnail != null) {
                        Image(
                            bitmap = thumbnail!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            FontAwesomeIcons.Solid.Video,
                            contentDescription = "Video",
                            tint = Gray400,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(20.dp)
                .background(Black300.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                FontAwesomeIcons.Solid.Times,
                contentDescription = "Usuń",
                tint = White100,
                modifier = Modifier.size(12.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(4.dp)
                .background(Teal100.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (mediaType == "photo") "IMG" else "VID",
                color = Black300,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
