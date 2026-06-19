package pl.skomunikacja.synclyapp.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Trash
import compose.icons.fontawesomeicons.solid.Video
import pl.skomunikacja.synclyapp.MediaPreviewGrid
import pl.skomunikacja.synclyapp.helpers.Utils.uriToFile
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.model.post.LinkPostRequest
import pl.skomunikacja.synclyapp.model.post.PhotoPostRequest
import pl.skomunikacja.synclyapp.model.post.QuotePostRequest
import pl.skomunikacja.synclyapp.model.post.TextPostRequest
import pl.skomunikacja.synclyapp.model.post.VideoPostRequest
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import java.io.File

@Composable
fun TextPostForm(
    tags: List<Tag>,
    onPostChange: (TextPostRequest) -> Unit,
    errorMessage: String?
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    val contentError = errorMessage == "Content cannot be blank."

    LaunchedEffect(title, content, selectedTags) {
        onPostChange(
            TextPostRequest(
                title = title,
                content = content,
                tags = selectedTags.map { it.name },
                type = "TEXT"
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (optional)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5,
            isError = contentError,
            supportingText = {
                if (contentError) {
                    Text(
                        text = errorMessage,
                        color = Red100
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        TagSelector(
            availableTags = tags,
            selectedTags = selectedTags,
            onTagsChange = { selectedTags = it }
        )
    }
}

@Composable
fun QuotePostForm(
    tags: List<Tag>,
    onPostChange: (QuotePostRequest) -> Unit,
    errorMessage: String?
) {
    var quote by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    val quoteError = errorMessage == "Quote cannot be blank."

    LaunchedEffect(quote, source, selectedTags) {
        onPostChange(
            QuotePostRequest(
                quote = quote,
                source = source,
                tags = selectedTags.map { it.name },
                type = "QUOTE"
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = quote,
            onValueChange = { quote = it },
            label = { Text("Quote", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 4,
            isError = quoteError,
            supportingText = {
                if (quoteError) {
                    Text(
                        text = errorMessage,
                        color = Red100
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        OutlinedTextField(
            value = source,
            onValueChange = { source = it },
            label = { Text("Source (optional)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        TagSelector(
            availableTags = tags,
            selectedTags = selectedTags,
            onTagsChange = { selectedTags = it }
        )
    }
}

@Composable
fun PhotoPostForm(
    tags: List<Tag>,
    onPostChange: (PhotoPostRequest) -> Unit,
    onFilesChange: (List<File>) -> Unit,
    errorMessage: String?
) {
    var caption by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    var selectedPhotos by remember { mutableStateOf(emptyList<Uri>()) }
    val context = LocalContext.current
    val captionError = errorMessage == "Caption cannot be blank."
    val photosError = errorMessage == "Add at least one photo."

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedPhotos = uris

            val files = uris.mapNotNull { uri ->
                uriToFile(context, uri)
            }
            onFilesChange(files)
        }
    }

    LaunchedEffect(caption, selectedTags, selectedPhotos) {
        onPostChange(
            PhotoPostRequest(
                caption = caption,
                tags = selectedTags.map { it.name },
                type = "PHOTO",
                photos = emptyList()
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(

            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(
                    2.dp,
                    if (selectedPhotos.isNotEmpty()) Teal100 else Gray500,
                    RoundedCornerShape(8.dp)
                )
                .clickable {
                    photoLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    if (selectedPhotos.isNotEmpty()) FontAwesomeIcons.Solid.Check else FontAwesomeIcons.Solid.Plus,
                    contentDescription = "Add photo",
                    tint = if (selectedPhotos.isNotEmpty()) Teal100 else Gray400,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (selectedPhotos.isNotEmpty()) "Selected ${selectedPhotos.size} photos" else "Add photos",
                    color = if (selectedPhotos.isNotEmpty()) Teal100 else Gray400,
                    fontSize = 14.sp
                )
            }

            if (photosError) {
                Text(
                    text = errorMessage,
                    color = Red100,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        if (selectedPhotos.isNotEmpty()) {
            MediaPreviewGrid(
                mediaUris = selectedPhotos,
                onRemoveMedia = { uri ->
                    selectedPhotos = selectedPhotos.filter { it != uri }
                },
                mediaType = "photo"
            )
        }


        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            label = { Text("Caption", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            isError = captionError,
            supportingText = {
                if (captionError) {
                    Text(
                        text = errorMessage,
                        color = Red100
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        TagSelector(
            availableTags = tags,
            selectedTags = selectedTags,
            onTagsChange = { selectedTags = it }
        )
    }
}

@Composable
fun VideoPostForm(
    tags: List<Tag>,
    onPostChange: (VideoPostRequest) -> Unit,
    errorMessage: String?
) {
    var description by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    var videoLink by remember { mutableStateOf("") }
    var videoLinks by remember { mutableStateOf(emptyList<String>()) }
    val descriptionError = errorMessage == "Description cannot be blank."
    val videoLinksError = errorMessage == "Add at least one video."

    LaunchedEffect(description, selectedTags, videoLinks) {
        onPostChange(
            VideoPostRequest(
                description = description,
                tags = selectedTags.map { it.name },
                type = "VIDEO",
                videoUrls = videoLinks
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = videoLink,
                onValueChange = { videoLink = it },
                label = { Text("Video link", color = Gray400) },
                placeholder = {
                    Text(
                        "https://youtube.com/...",
                        color = Gray500
                    )
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = videoLinksError,
                supportingText = {
                    if (videoLinksError) {
                        Text(
                            text = errorMessage,
                            color = Red100
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White100,
                    unfocusedTextColor = White100,
                    focusedBorderColor = Teal100,
                    unfocusedBorderColor = Gray500,
                    errorBorderColor = Red100,
                    errorLabelColor = Red100
                )
            )

            Button(
                modifier = Modifier.height(55.dp).padding(top = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal100,
                    contentColor = Black300
                ),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    val trimmedLink = videoLink.trim()

                    if (trimmedLink.isNotEmpty() && trimmedLink !in videoLinks) {
                        videoLinks = videoLinks + trimmedLink
                        videoLink = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        if (videoLinks.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                videoLinks.forEach { link ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                Gray500,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = link,
                            color = White100,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        IconButton(
                            onClick = {
                                videoLinks = videoLinks.filter { it != link }
                            }
                        ) {
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.Trash,
                                contentDescription = "Remove video link",
                                tint = Gray400,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description", color = Gray400) },
            isError = descriptionError,
            supportingText = {
                if (descriptionError) {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = Red100
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        TagSelector(
            availableTags = tags,
            selectedTags = selectedTags,
            onTagsChange = { selectedTags = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkPostForm(
    tags: List<Tag>,
    onPostChange: (LinkPostRequest) -> Unit,
    errorMessage: String?
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var links by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    val descriptionError = errorMessage == "Description cannot be blank."
    val linksError = errorMessage == "Add at least one link." ||
            errorMessage == "Link cannot be empty."

    LaunchedEffect(title, description, selectedTags, links) {
        val mappedLinks = links
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        onPostChange(
            LinkPostRequest(
                title = title,
                description = description,
                tags = selectedTags.map { it.name },
                type = "LINK",
                links = mappedLinks
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (optional)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            isError = descriptionError,
            supportingText = {
                if (descriptionError) {
                    Text(
                        text = errorMessage,
                        color = Red100
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        OutlinedTextField(
            value = links,
            onValueChange = { links = it },
            label = { Text("Links (separate with comma)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            isError = linksError,
            supportingText = {
                if (linksError) {
                    Text(
                        text = errorMessage,
                        color = Red100
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                errorBorderColor = Red100,
                errorLabelColor = Red100
            )
        )

        TagSelector(
            availableTags = tags,
            selectedTags = selectedTags,
            onTagsChange = { selectedTags = it }
        )
    }
}