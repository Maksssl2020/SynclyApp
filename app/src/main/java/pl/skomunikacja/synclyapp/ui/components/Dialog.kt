package pl.skomunikacja.synclyapp.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Image
import pl.skomunikacja.synclyapp.model.AvatarUploadState
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100

@Composable
fun AvatarUploadDialog(
    uploadState: AvatarUploadState,
    handleFileChange: (Uri?) -> Unit,
    onDismiss: () -> Unit,
    onSaveAvatar: (String) -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        handleFileChange(uri)
    }

    AlertDialog(
        containerColor = Black300,
        icon = {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Image,
                tint = Teal100,
                contentDescription = "upload avatar",
                modifier = Modifier.size(32.dp)
            )
        },
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Upload Avatar",
                color = White100,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Select a picture from your gallery, crop it and save as your new avatar.",
                    color = Gray300,
                    fontSize = 14.sp
                )

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal100,
                        contentColor = Black300
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.Image,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Picture", fontWeight = FontWeight.Medium)
                }

                uploadState.avatarBase64?.let { base64 ->
                    val bitmap = remember(base64) {
                        base64ToBitmap(base64)
                    }

                    bitmap?.let {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Preview:",
                                color = Gray300,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Avatar Preview",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    uploadState.avatarBase64?.let { onSaveAvatar(it) }
                },
                enabled = uploadState.avatarBase64 != null && !uploadState.uploadingAvatar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal100,
                    contentColor = Black300,
                    disabledContainerColor = Gray300,
                    disabledContentColor = Black300
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (uploadState.uploadingAvatar) "Saving..." else "Save",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Gray300)
            ) {
                Text("Cancel")
            }
        }
    )
}


fun base64ToBitmap(base64: String): Bitmap? {
    return try {
        val pureBase64 = base64.substringAfter("base64,", base64)
        val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}
