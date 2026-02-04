package pl.skomunikacja.synclyapp

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.File
import compose.icons.fontawesomeicons.solid.Hashtag
import compose.icons.fontawesomeicons.solid.Image
import compose.icons.fontawesomeicons.solid.Link
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.QuoteLeft
import compose.icons.fontawesomeicons.solid.Times
import compose.icons.fontawesomeicons.solid.Video
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.model.post.LinkPostRequest
import pl.skomunikacja.synclyapp.model.post.PhotoPostRequest
import pl.skomunikacja.synclyapp.model.post.PostRequest
import pl.skomunikacja.synclyapp.model.post.PostType
import pl.skomunikacja.synclyapp.model.post.QuotePostRequest
import pl.skomunikacja.synclyapp.model.post.TextPostRequest
import pl.skomunikacja.synclyapp.model.post.VideoPostRequest
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.CreatePostViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun AddPostScreen(
    onPostCreated: () -> Unit = {},
    viewModel: CreatePostViewModel = viewModel()
) {
    val context = LocalContext.current
    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val tags by viewModel.tags.collectAsState()
    var selectedPostType by remember { mutableStateOf(PostType.TEXT) }
    var isPosting by remember { mutableStateOf(false) }
    var postRequest: PostRequest? = null
    var files: List<File> = emptyList()

    LaunchedEffect(Unit) {
        viewModel.fetchAllTags()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black300)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                text = "Nowy post",
                color = White100,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = {
                    isPosting = true
                    println(postRequest.toString())
                    if (authenticationData != null && postRequest != null) {
                        println("CREATING POST")
                        viewModel.createPost(
                            authenticationData!!.userId,
                            postRequest!!,
                            files,
                            {
                                Toast.makeText(context, "Successfully created post!", Toast.LENGTH_SHORT).show()
                                isPosting = false
                                onPostCreated()
                            },
                            {
                                println(it)
                                Toast.makeText(context, "There was an error during post creation. Try again.", Toast.LENGTH_SHORT).show()
                                isPosting = false
                            }
                        )
                    }
                },
                enabled = !isPosting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal100,
                    contentColor = Black300
                ),
                modifier = Modifier.height(36.dp)
            ) {
                if (isPosting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Black300,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Opublikuj", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Wybierz typ postu",
            color = White100,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(PostType.entries.toTypedArray()) { postType ->
                PostTypeChip(
                    postType = postType,
                    isSelected = selectedPostType == postType,
                    onClick = { selectedPostType = postType }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedPostType) {
                PostType.TEXT -> TextPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    }
                )
                PostType.QUOTE -> QuotePostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    }
                )
                PostType.PHOTO -> PhotoPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    },
                    onFilesChange = {
                        files = it
                    }
                )
                PostType.VIDEO -> VideoPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    },
                    onFilesChange = {
                        files = it
                    }
                )
                PostType.LINK -> LinkPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    }
                )
            }
        }
    }
}

@Composable
fun PostTypeChip(
    postType: PostType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (icon, label) = when (postType) {
        PostType.TEXT -> FontAwesomeIcons.Solid.File to "Tekst"
        PostType.QUOTE -> FontAwesomeIcons.Solid.QuoteLeft to "Cytat"
        PostType.PHOTO -> FontAwesomeIcons.Solid.Image to "Zdjęcie"
        PostType.VIDEO -> FontAwesomeIcons.Solid.Video to "Wideo"
        PostType.LINK -> FontAwesomeIcons.Solid.Link to "Link"
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Teal100 else Black200)
            .border(
                1.dp,
                if (isSelected) Teal100 else Gray500,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (isSelected) Black300 else White100,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            color = if (isSelected) Black300 else White100,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TextPostForm(
    tags: List<Tag>,
    onPostChange: (TextPostRequest) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }

    LaunchedEffect(title, content, selectedTags) {
        onPostChange(
            TextPostRequest(
                title = title,
                content = content,
                tags = selectedTags.map { it.name },
                type = "text"
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł (opcjonalnie)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
            )
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Treść", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
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
    onPostChange: (QuotePostRequest) -> Unit
) {
    var quote by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }

    LaunchedEffect(quote, source, selectedTags) {
        onPostChange(
            QuotePostRequest(
                quote = quote,
                source = source,
                tags = selectedTags.map { it.name },
                type = "quote"
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = quote,
            onValueChange = { quote = it },
            label = { Text("Cytat", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
            )
        )

        OutlinedTextField(
            value = source,
            onValueChange = { source = it },
            label = { Text("Źródło (opcjonalnie)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
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
    onFilesChange: (List<File>) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    var selectedPhotos by remember { mutableStateOf(emptyList<Uri>()) }
    val context = LocalContext.current

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedPhotos = uris
            println("Wybrane zdjęcia: $uris")

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
                type = "photo",
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
                    contentDescription = "Dodaj zdjęcie",
                    tint = if (selectedPhotos.isNotEmpty()) Teal100 else Gray400,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (selectedPhotos.isNotEmpty()) "Wybrano ${selectedPhotos.size} zdjęć" else "Dodaj zdjęcia",
                    color = if (selectedPhotos.isNotEmpty()) Teal100 else Gray400,
                    fontSize = 14.sp
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
            label = { Text("Opis", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
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
    onFilesChange: (List<File>) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    var selectedVideos by remember { mutableStateOf(emptyList<Uri>()) }
    val context = LocalContext.current

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedVideos = uris
            println("Wybrane wideo: $uris")

            val files = uris.mapNotNull { uri ->
                uriToFile(context, uri)
            }
            onFilesChange(files)
        }
    }

    LaunchedEffect(description, selectedTags, selectedVideos) {
        onPostChange(
            VideoPostRequest(
                description = description,
                tags = selectedTags.map { it.name },
                type = "video",
                videos = emptyList()
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
                    if (selectedVideos.isNotEmpty()) Teal100 else Gray500,
                    RoundedCornerShape(8.dp)
                )
                .clickable {
                    videoLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
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
                    if (selectedVideos.isNotEmpty()) FontAwesomeIcons.Solid.Check else FontAwesomeIcons.Solid.Video,
                    contentDescription = "Dodaj wideo",
                    tint = if (selectedVideos.isNotEmpty()) Teal100 else Gray400,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (selectedVideos.isNotEmpty()) "Wybrano ${selectedVideos.size} filmów" else "Dodaj wideo",
                    color = if (selectedVideos.isNotEmpty()) Teal100 else Gray400,
                    fontSize = 14.sp
                )
            }
        }

        if (selectedVideos.isNotEmpty()) {
            MediaPreviewGrid(
                mediaUris = selectedVideos,
                onRemoveMedia = { uri ->
                    selectedVideos = selectedVideos.filter { it != uri }
                },
                mediaType = "video"
            )
        }
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
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
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var links by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<Tag>()) }
    var mappedLinks by remember {mutableStateOf(emptyList<String>())}

    LaunchedEffect(links) {
        val linksParts = links.trim().split(",")
        mappedLinks = linksParts.map { linksPart -> linksPart.trim()}
    }

    LaunchedEffect(title, description, selectedTags, links) {
        onPostChange(
            LinkPostRequest(
                description = description,
                tags = selectedTags.map { it.name },
                type = "link",
                links = mappedLinks
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł (opcjonalnie)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
            )
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis", color = Gray400) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
            )
        )

        OutlinedTextField(
            value = links,
            onValueChange = { links = it },
            label = { Text("Linki (oddziel przecinkami)", color = Gray400) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500
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
fun TagSelector(
    availableTags: List<Tag>,
    selectedTags: List<Tag>,
    onTagsChange: (List<Tag>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Selected tags display
        if (selectedTags.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedTags) { tag ->
                    TagChip(
                        tag = tag,
                        isSelected = true,
                        onRemove = {
                            onTagsChange(selectedTags - tag)
                        }
                    )
                }
            }
        }

        // Tag selector dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = "Wybierz tagi",
                onValueChange = { },
                readOnly = true,
                label = { Text("Tagi", color = Gray400) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White100,
                    unfocusedTextColor = White100,
                    focusedBorderColor = Teal100,
                    unfocusedBorderColor = Gray500
                ),
                leadingIcon = {
                    Icon(
                        FontAwesomeIcons.Solid.Hashtag,
                        contentDescription = "Tagi",
                        tint = Gray400,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Black200)
            ) {
                availableTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            Color(tag.color.toColorInt()),
                                            RoundedCornerShape(6.dp)
                                        )
                                )
                                Text(
                                    text = tag.name,
                                    color = if (isSelected) Teal100 else White100
                                )
                                if (isSelected) {
                                    Icon(
                                        FontAwesomeIcons.Solid.Check,
                                        contentDescription = "Wybrane",
                                        tint = Teal100,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            if (isSelected) {
                                onTagsChange(selectedTags - tag)
                            } else {
                                onTagsChange(selectedTags + tag)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TagChip(
    tag: Tag,
    isSelected: Boolean,
    onRemove: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(
                Color(tag.color.toColorInt()).copy(alpha = 0.2f),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Color(tag.color.toColorInt()),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    Color(tag.color.toColorInt()),
                    RoundedCornerShape(4.dp)
                )
        )
        Text(
            text = tag.name,
            color = White100,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        if (isSelected) {
            Icon(
                FontAwesomeIcons.Solid.Times,
                contentDescription = "Usuń tag",
                tint = Gray400,
                modifier = Modifier
                    .size(12.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

@Composable
fun MediaPickerExample() {
    val context = LocalContext.current

    // Wynik z pickera -> list<Uri>
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            println("Wybrane pliki: $uris")
            // tutaj możesz np. dodać do listy zdjęć/filmów do wysłania
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable {
                launcher.launch(
                    PickVisualMediaRequest(
                        // Możesz ograniczyć do zdjęć lub wideo
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text("Dodaj zdjęcia/filmy")
    }
}

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
        // Media preview placeholder with icon
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

        // Delete button in top-right corner
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

        // Media type indicator in bottom-left corner
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

fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val bitmap = retriever.getFrameAtTime(0) // klatka z początku filmu
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
