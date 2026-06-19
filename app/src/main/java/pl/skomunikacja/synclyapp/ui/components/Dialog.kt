package pl.skomunikacja.synclyapp.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Flag
import compose.icons.fontawesomeicons.solid.Image
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.helpers.base64ToBitmap
import pl.skomunikacja.synclyapp.model.AvatarUploadState
import pl.skomunikacja.synclyapp.model.CreateReportRequest
import pl.skomunikacja.synclyapp.model.ReportReason
import pl.skomunikacja.synclyapp.model.ReportType
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray600
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.ReportViewModel

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
                    "Select a picture from your gallery and save as your new avatar.",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPostModal(
    isVisible: Boolean,
    postId: Long,
    authorId: Long,
    authorName: String,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModel: ReportViewModel = viewModel()

    var selectedReason by remember { mutableStateOf(ReportReason.OTHER) }
    var title by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = White100,
        unfocusedTextColor = White100,
        focusedContainerColor = Black300,
        unfocusedContainerColor = Black300,
        focusedBorderColor = Teal100,
        unfocusedBorderColor = Gray600,
        focusedLabelColor = Teal100,
        unfocusedLabelColor = Gray400,
        cursorColor = Teal100,
        errorBorderColor = Red100,
        errorLabelColor = Red100
    )

    Dialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = Black200,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Red100.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.Flag,
                            contentDescription = null,
                            tint = Red100,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Report post",
                            color = White100,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Author: ${authorName.ifBlank { "user" }}",
                            color = Gray400,
                            fontSize = 13.sp
                        )
                    }

                    IconButton(
                        onClick = { if (!isLoading) onDismiss() }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Gray400
                        )
                    }
                }

                HorizontalDivider(color = Gray600.copy(alpha = 0.5f))

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedReason.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Report type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                        },
                        colors = fieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.background(Black300)
                    ) {
                        ReportReason.entries.forEach { reasonType ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = reasonType.toString(),
                                        color = if (reasonType == selectedReason) Teal100 else White100
                                    )
                                },
                                onClick = {
                                    selectedReason = reasonType
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    placeholder = { Text("Briefly describe the problem", color = Gray400) },
                    singleLine = true,
                    isError = title.isBlank() && errorMessage != null,
                    colors = fieldColors,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason") },
                    placeholder = { Text("Describe the details", color = Gray400) },
                    minLines = 4,
                    maxLines = 8,
                    isError = reason.isBlank() && errorMessage != null,
                    colors = fieldColors,
                    shape = RoundedCornerShape(12.dp),
                    supportingText = {
                        Text(
                            text = "${reason.length} characters",
                            color = Gray400,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 130.dp)
                )

                errorMessage?.let { message ->
                    Surface(
                        color = Red100.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
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

                RowButtons(
                    isLoading = isLoading,
                    onCancel = onDismiss,
                    onSend = {
                        if (title.isBlank()) {
                            errorMessage = "Title cannot be empty."
                            return@RowButtons
                        }

                        if (reason.isBlank()) {
                            errorMessage = "Reason cannot be empty."
                            return@RowButtons
                        }

                        coroutineScope.launch {
                            isLoading = true
                            errorMessage = null

                            try {
                                viewModel.createReport(
                                    CreateReportRequest(
                                        userId = authorId,
                                        entityId = postId,
                                        reportReasonType = selectedReason,
                                        title = title,
                                        reason = reason,
                                        reportType = ReportType.POST
                                    ),
                                    {
                                        Toast.makeText(context, "Report has been sent!", Toast.LENGTH_SHORT).show()
                                    },
                                    {
                                        println(it)
                                        Toast.makeText(context, "There was an error submitting your request. Please try again.", Toast.LENGTH_SHORT).show()
                                    }
                                )

                                title = ""
                                reason = ""
                                selectedReason = ReportReason.OTHER
                                onDismiss()
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Unable to submit report."
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            }
        }
    }
}

