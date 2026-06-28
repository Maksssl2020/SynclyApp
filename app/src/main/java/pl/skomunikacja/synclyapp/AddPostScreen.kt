package pl.skomunikacja.synclyapp

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times
import compose.icons.fontawesomeicons.solid.Video
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.Utils.getVideoThumbnail
import pl.skomunikacja.synclyapp.model.post.LinkPostRequest
import pl.skomunikacja.synclyapp.model.post.PhotoPostRequest
import pl.skomunikacja.synclyapp.model.post.PostRequest
import pl.skomunikacja.synclyapp.model.post.PostType
import pl.skomunikacja.synclyapp.model.post.QuotePostRequest
import pl.skomunikacja.synclyapp.model.post.TextPostRequest
import pl.skomunikacja.synclyapp.model.post.VideoPostRequest
import pl.skomunikacja.synclyapp.ui.components.LinkPostForm
import pl.skomunikacja.synclyapp.ui.components.PhotoPostForm
import pl.skomunikacja.synclyapp.ui.components.PostTypeChip
import pl.skomunikacja.synclyapp.ui.components.QuotePostForm
import pl.skomunikacja.synclyapp.ui.components.TextPostForm
import pl.skomunikacja.synclyapp.ui.components.VideoPostForm
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.CreatePostViewModel
import java.io.File

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
    var postRequest by remember { mutableStateOf<PostRequest?>(null) }
    var files by remember { mutableStateOf<List<File>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                text = "New post",
                color = White100,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = {
                    val validationError = validatePostRequest(
                        postType = selectedPostType,
                        postRequest = postRequest,
                        files = files
                    )

                    if (validationError != null) {
                        errorMessage = validationError
                        return@Button
                    }

                    val userId = authenticationData?.userId

                    if (userId == null) {
                        errorMessage = "You must be logged in to create a post."
                        return@Button
                    }

                    val request = postRequest ?: run {
                        errorMessage = "Fill in the post form."
                        return@Button
                    }

                    errorMessage = null
                    isPosting = true

                    viewModel.createPost(
                        userId,
                        request,
                        files,
                        {
                            Toast.makeText(context, "Successfully created post!", Toast.LENGTH_SHORT).show()
                            isPosting = false
                            errorMessage = null
                            onPostCreated()
                        },
                        {
                            Toast.makeText(
                                context,
                                "There was an error during post creation. Try again.",
                                Toast.LENGTH_SHORT
                            ).show()

                            isPosting = false
                        }
                    )
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
                    Text("Public", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

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

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Select post type",
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
                    onClick = {
                        selectedPostType = postType
                        postRequest = null
                        files = emptyList()
                        errorMessage = null
                    }
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
                    },
                    errorMessage = errorMessage
                )
                PostType.QUOTE -> QuotePostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    },
                    errorMessage = errorMessage
                )
                PostType.PHOTO -> PhotoPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    },
                    onFilesChange = {
                        files = it
                    },
                    errorMessage = errorMessage
                )
                PostType.VIDEO -> VideoPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    },
                    errorMessage = errorMessage
                )
                PostType.LINK -> LinkPostForm(
                    tags = tags,
                    onPostChange = {
                        postRequest = it
                    },
                    errorMessage = errorMessage
                )
            }
        }
    }
}

private fun validatePostRequest(
    postType: PostType,
    postRequest: PostRequest?,
    files: List<File>
): String? {
    if (postRequest == null) {
        return "Fill in the post form."
    }

    return when (postType) {
        PostType.TEXT -> {
            val request = postRequest as? TextPostRequest
                ?: return "Invalid text post data."

            if (request.content.isBlank()) {
                "Content cannot be blank."
            } else {
                null
            }
        }

        PostType.QUOTE -> {
            val request = postRequest as? QuotePostRequest
                ?: return "Invalid quote post data."

            if (request.quote.isBlank()) {
                "Quote cannot be blank."
            } else {
                null
            }
        }

        PostType.PHOTO -> {
            val request = postRequest as? PhotoPostRequest
                ?: return "Invalid photo post data."

            when {
                request.caption.isBlank() -> "Caption cannot be blank."
                files.isEmpty() -> "Add at least one photo."
                else -> null
            }
        }

        PostType.VIDEO -> {
            val request = postRequest as? VideoPostRequest
                ?: return "Invalid video post data."

            when {
                request.description.isBlank() -> "Description cannot be blank."
                request.videoUrls.isEmpty() -> "Add at least one video."
                else -> null
            }
        }

        PostType.LINK -> {
            val request = postRequest as? LinkPostRequest
                ?: return "Invalid link post data."

            when {
                request.description.isBlank() -> "Description cannot be blank."
                request.links.isEmpty() -> "Add at least one link."
                request.links.any { it.isBlank() } -> "Link cannot be empty."
                else -> null
            }
        }
    }
}


