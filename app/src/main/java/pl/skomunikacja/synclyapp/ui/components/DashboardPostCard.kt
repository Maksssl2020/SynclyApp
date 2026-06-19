package pl.skomunikacja.synclyapp.ui.components

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import coil.compose.AsyncImage
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.Comment
import compose.icons.fontawesomeicons.solid.Link
import compose.icons.fontawesomeicons.solid.QuoteLeft
import pl.skomunikacja.synclyapp.helpers.Utils.timeAgoOrDate
import pl.skomunikacja.synclyapp.helpers.isDirectVideoUrl
import pl.skomunikacja.synclyapp.helpers.isYoutubeUrl
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.post.LinkPost
import pl.skomunikacja.synclyapp.model.post.PhotoPost
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.model.post.PostActionType
import pl.skomunikacja.synclyapp.model.post.QuotePost
import pl.skomunikacja.synclyapp.model.post.TextPost
import pl.skomunikacja.synclyapp.model.post.VideoPost
import pl.skomunikacja.synclyapp.ui.theme.Black100
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray600
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.PostViewModel
import kotlin.math.log

@Composable
fun DashboardPostCard(
    post: Post,
    currentUserId: Long = 1,
    onAuthorClick: (Long) -> Unit = {},
    userPostCollections: List<PostCollection>
) {
    var isSaved by remember { mutableStateOf(post.savedBy.contains(currentUserId)) }
    var showComments by remember { mutableStateOf(false) }
    var showSaveModal by remember { mutableStateOf(false) }
    var showPhotoModal by remember { mutableStateOf(false) }
    var showVideoModal by remember { mutableStateOf(false) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showReportModal by remember { mutableStateOf(false) }
    var selectedMediaIndex by remember { mutableIntStateOf(0) }

    val timeAgoOrDate = timeAgoOrDate(post.createdAt)

    val viewModel = remember(post.id) { PostViewModel(post) }
    val state by viewModel.post.collectAsState()

    val isLiked = state.likesBy.contains(currentUserId)
    val isShared = state.sharedBy.contains(currentUserId)
    val likesCount = state.likesBy.size;
    val sharesCount = state.sharedBy.size;
    val comments = viewModel.postComments.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Black200
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Gray600)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileAvatar(
                    onAvatarClick = {},
                    base64Image = post.authorAvatar?.imageData,
                    initials = post.authorName.first().toString(),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        color = White100,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onAuthorClick(post.authorId) }
                    )
                    Row {
                        Text(
                            text = "@${post.authorUsername}",
                            color = Gray400,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { onAuthorClick(post.authorId) }
                        )

                    }
                    Text(
                        text = timeAgoOrDate,
                        color = Gray400,
                        fontSize = 10.sp
                    )
                }

                Box {
                    IconButton(onClick = { showOptionsMenu = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Gray400
                        )
                    }

                    DropdownMenu(
                        expanded = showOptionsMenu,
                        onDismissRequest = { showOptionsMenu = false },
                        containerColor = Black200
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Report post",
                                    color = White100
                                )
                            },
                            onClick = {
                                showOptionsMenu = false
                                showReportModal = true
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GetPostDataDisplay(
                post,
                { showModal: Boolean, mediaIndex: Int ->
                    showVideoModal = showModal
                    selectedMediaIndex = mediaIndex
                },
                { showModal: Boolean, mediaIndex: Int ->
                    showPhotoModal = showModal
                    selectedMediaIndex = mediaIndex
                }
            )

            if (post.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(post.tags) { tag ->
                        Surface(
                            color = Color(tag.color.toColorInt()),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "#${tag.name}",
                                color = Black100,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Gray600, thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Surface(
                        color = if (isLiked) Color(0xFF4D3232) else Color(0xFF222222),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isLiked) Color(0xFF4D3232) else Color(0xFF222222)
                        ),
                        modifier = Modifier.clickable {
                            viewModel.toggleAction(currentUserId, PostActionType.LIKE)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Like",
                                tint = Color(0xFFB0B0B0),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = likesCount.toString(),
                                color = Color(0xFFB0B0B0),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Surface(
                        color = if (showComments) Color(0xFF22454B) else Color(0xFF222222),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable {
                            showComments = !showComments
                            viewModel.showPostComments()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                FontAwesomeIcons.Solid.Comment,
                                contentDescription = "Comment",
                                tint = Color(0xFFB0B0B0),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = comments.value.size.toString(),
                                color = Color(0xFFB0B0B0),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Surface(
                        color = if (isShared) Color(0xFF244541) else Color(0xFF222222),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable {
                            viewModel.toggleAction(currentUserId, PostActionType.SHARE)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color(0xFFB0B0B0),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = sharesCount.toString(),
                                color = Color(0xFFB0B0B0),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Surface(
                    color = if (isSaved) Color(0xFF4D441F) else Color(0xFF222222),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSaved) Color(0xFF4D441F) else Color(0xFF222222)
                    ),
                    modifier = Modifier.clickable {
                        showSaveModal = true
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.Bookmark,
                            contentDescription = "Save",
                            tint = Color(0xFFB0B0B0),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (showComments) {
                PostCommentsSection(
                    post.id,
                    viewModel,
                    currentUserId,
                    comments.value,
                    {
                        onAuthorClick(post.authorId)
                    }
                )
            }
        }
    }

    if (post is PhotoPost) {
        PhotoViewerModal(
            isVisible = showPhotoModal,
            imageUrls = post.imageUrls,
            initialIndex = selectedMediaIndex,
            onDismiss = { showPhotoModal = false }
        )
    }

    if (post is VideoPost) {
        val directVideoUrls = post.videoUrls.filter { isDirectVideoUrl(it) }

        VideoViewerModal(
            isVisible = showVideoModal,
            videoUrls = directVideoUrls,
            initialIndex = selectedMediaIndex.coerceAtMost(directVideoUrls.lastIndex),
            onDismiss = { showVideoModal = false }
        )
    }

    SaveToCollectionModal(
        post = post,
        isVisible = showSaveModal,
        collections = userPostCollections,
        onDismiss = { showSaveModal = false },
        onCollectionSelected = { collection ->
            showSaveModal = false
        },
        onIsSavedChange = {it ->
            isSaved = it
        }
    )

    ReportPostModal(
        isVisible = showReportModal,
        postId = post.id,
        authorName = post.authorName.orEmpty(),
        onDismiss = { showReportModal = false },
        authorId = currentUserId
    )
}

@Composable
private fun GetPostDataDisplay(
    post: Post,
    onShowVideoModalClick: (Boolean, Int) -> Unit,
    onShowPhotoModalClick: (Boolean, Int) -> Unit,
) {
    val context = LocalContext.current

    when (post) {
        is TextPost -> {
            if (!post.title.isNullOrEmpty()) {
                Text(
                    text = post.title,
                    color = White100,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = post.content,
                color = White100,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
        is QuotePost -> {
            Surface(
                color = Black300,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Teal100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Icon(
                            FontAwesomeIcons.Solid.QuoteLeft,
                            contentDescription = "Quote",
                            tint = Teal100,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cytat",
                            color = Teal100,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\"${post.quote}\"",
                        color = White100,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "— ${post.source}",
                        color = Gray400,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        is PhotoPost -> {
            post.imageUrls.forEach {
                Log.d("PHOTO_URL", it)
            }

            if (post.caption.isNotEmpty()) {
                Text(
                    text = post.caption,
                    color = White100,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (post.imageUrls.size == 1) {
                PostImage(
                    imageValue = post.imageUrls[0],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onShowPhotoModalClick(true, 0)
                        }
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(post.imageUrls.size) { index ->
                        val imageUrl = post.imageUrls[index]

                        PostImage(
                            imageValue = imageUrl,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onShowPhotoModalClick(true, index)
                                }
                        )
                    }
                }
            }
        }
        is VideoPost -> {
            post.videoUrls.forEach {
                Log.d("VIDEO_URL", it)
            }

            if (post.description.isNotEmpty()) {
                Text(
                    text = post.description,
                    color = White100,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(post.videoUrls.size) { index ->
                    val videoUrl = post.videoUrls[index]
                    if (isDirectVideoUrl(videoUrl)) {
                        VideoThumbnail(videoUrl) {
                            onShowVideoModalClick(true, index)
                        }
                    } else if (isYoutubeUrl(videoUrl)) {
                        YoutubeLinkCard(videoUrl)
                    } else {
                        Text(
                            text = "Unsupported video format",
                            color = Gray400,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        is LinkPost -> {
            post.title?.let {
                Text(
                    text = it,
                    color = White100,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.description,
                color = White100,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            post.urls.forEach { url ->
                Surface(
                    color = Black300,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Teal100),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.Link,
                            contentDescription = "Link",
                            tint = Teal100,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = url,
                            color = Teal100,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

