package pl.skomunikacja.synclyapp

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.parseHexColor
import pl.skomunikacja.synclyapp.model.CollectionColor
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray600
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.Teal200
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.PostCollectionsViewModel

private val AVAILABLE_COLORS = listOf(
    CollectionColor("Teal Dark", "#0d9488"),
    CollectionColor("Cyan Dark", "#06b6d4"),
    CollectionColor("Green", "#22c55e"),
    CollectionColor("Blue", "#3b82f6"),
    CollectionColor("Purple", "#a855f7"),
    CollectionColor("Pink", "#ec4899"),
    CollectionColor("Orange", "#f97316"),
    CollectionColor("Red", "#ef4444"),
    CollectionColor("Yellow", "#eab308"),
    CollectionColor("Indigo", "#6366f1"),
)

@Composable
fun PostCollectionFormScreen(
    onGoBack: () -> Unit,
    viewModel: PostCollectionsViewModel = viewModel(),
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(AVAILABLE_COLORS.first().colorValue) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val creatingPostCollection by viewModel.creatingPostCollection.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        errorMessage?.let { message ->
            Surface(
                color = Red100.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Red100,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = message,
                        color = Red100,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Collection title",
                color = White100,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Collection title", color = Gray400, fontSize = 14.sp)
                },
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                isError = errorMessage == "Title cannot be blank.",
                supportingText = {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Red100
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal100,
                    unfocusedBorderColor = Gray600,
                    focusedTextColor = White100,
                    unfocusedTextColor = White100,
                    cursorColor = Teal100,
                    focusedContainerColor = Black300,
                    unfocusedContainerColor = Black300,
                    errorBorderColor = Red100
                )
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Collection Color",
                color = White100,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            AVAILABLE_COLORS.chunked(4).forEach { rowColors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowColors.forEach { color ->
                        ColorSelect(
                            color = color.colorValue,
                            selected = selectedColor == color.colorValue,
                            onClick = { selectedColor = color.colorValue },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - rowColors.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Surface(
            color = Black300,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, Gray600),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(parseHexColor(selectedColor))
                    )
                    Text(
                        text = title.ifBlank { "Collection Name" },
                        color = White100,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "0 posts",
                    color = Gray400,
                    fontSize = 14.sp
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = Black200,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onGoBack() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Back",
                        color = Gray400,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Surface(
                color = if (creatingPostCollection) Teal200 else Teal100,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = !creatingPostCollection) {
                        val userId = authenticationData?.userId

                        if (userId == null) {
                            errorMessage = "You must be logged in to create a post."
                            return@clickable
                        }

                        if (title.isNotBlank()) {
                            viewModel.createPostCollection(userId, title, selectedColor, {
                                Toast.makeText(context, "Successfully created post collection!", Toast.LENGTH_SHORT).show()
                                selectedColor = AVAILABLE_COLORS.first().colorValue
                                title = ""
                            })
                        } else {
                            errorMessage = "Title cannot be blank."
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (creatingPostCollection) {
                        CircularProgressIndicator(
                            color = Black400,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Creating...",
                            color = Black400,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Black400,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Create & Save",
                            color = Black400,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColorSelect(color: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    TODO("Not yet implemented")
}

