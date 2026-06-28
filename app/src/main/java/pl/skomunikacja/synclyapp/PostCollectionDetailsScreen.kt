package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.FolderOpen
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.PostCollectionsManager
import pl.skomunikacja.synclyapp.ui.components.DashboardPostCard
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.ui.theme.White200
import pl.skomunikacja.synclyapp.ui.theme.White300
import pl.skomunikacja.synclyapp.view_model.PostCollectionsViewModel

@Composable
fun PostCollectionDetailsScreen(
    collectionId: Long,
    onBackClick: () -> Unit,
    viewModel: PostCollectionsViewModel = viewModel()
) {
    val userPostCollections by PostCollectionsManager.userPostCollections.collectAsState()
    val collection by viewModel.selectedPostCollection.collectAsState()

    LaunchedEffect(collectionId) {
        viewModel.loadPostCollectionById(collectionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
    ) {
        if (collection == null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = White100
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Post Collection",
                    color = White100,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                color = Black300,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.FolderOpen,
                        contentDescription = null,
                        tint = White300,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Post collection not found",
                        color = White200,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This collection may have been deleted or you may not have access to it",
                        color = White300,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = White100
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(collection!!.color.toColorInt()))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = collection!!.title,
                        color = White100,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${collection!!.posts.size} ${if (collection!!.posts.size == 1) "post" else "posts"}",
                        color = White200,
                        fontSize = 14.sp
                    )
                }

            }

            HorizontalDivider(
                color = Black300,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (collection!!.posts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.FolderOpen,
                            contentDescription = null,
                            tint = White300,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No posts",
                            color = White200,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This collection is empty. Add posts to view them here",
                            color = White300,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(collection!!.posts) { post ->
                        DashboardPostCard(
                            post = post,
                            userPostCollections = userPostCollections
                        )
                    }
                }
            }
        }
    }
}