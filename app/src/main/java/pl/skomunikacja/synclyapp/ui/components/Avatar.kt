package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pl.skomunikacja.synclyapp.helpers.Utils.rememberBase64Image
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Teal100

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    initials: String,
    base64Image: String?,
) {
    val imageBitmap = rememberBase64Image(base64Image)

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Profile avatar",
            modifier = modifier.clickable(
                onClick = onAvatarClick
            ),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier.background(Teal100).clickable(
                onClick = onAvatarClick
            ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials,
                color = Black300,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
        }
    }
}
