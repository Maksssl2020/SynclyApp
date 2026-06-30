package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ChevronRight
import compose.icons.fontawesomeicons.solid.FolderOpen
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray600
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.PostCollectionsViewModel

@Composable
fun SaveToCollectionModal(
    post: Post,
    isVisible: Boolean,
    collections: List<PostCollection>,
    onDismiss: () -> Unit,
    onCollectionSelected: (PostCollection) -> Unit,
    onIsSavedChange: (Boolean) -> Unit,
    viewModel: PostCollectionsViewModel = viewModel()
) {
    val filteredPostCollections = collections.filter { !it.default }

    println(collections)

    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = Black200
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Save to collection",
                            color = White100,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "close",
                                tint = Gray400
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (filteredPostCollections.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                FontAwesomeIcons.Solid.FolderOpen,
                                contentDescription = "No collections",
                                tint = Gray400,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No collections",
                                color = White100,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.heightIn(max = 400.dp)
                        ) {
                            items(filteredPostCollections) { collection ->
                                val mappedPostsIds = collection.posts.map(Post::id)
                                val isSavedToCollection = mappedPostsIds.contains(post.id)

                                CollectionItem(
                                    isSavedToCollection = isSavedToCollection,
                                    collection = collection,
                                    onClick = {
                                        if(isSavedToCollection) {
                                            viewModel.unsavePostFromCollection(collection.id, post.id) {
                                                onIsSavedChange(false)
                                            }
                                        } else {
                                            viewModel.savePostToCollection(collection.id, post) {
                                                onIsSavedChange(true)
                                            }
                                        }

                                        onCollectionSelected(collection)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionItem(
    isSavedToCollection: Boolean = false,
    collection: PostCollection,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSavedToCollection) Color(collection.color.toColorInt()).copy(0.2f) else Black300,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Gray600),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = Color(collection.color.toColorInt()),
                        shape = RoundedCornerShape(6.dp)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = collection.title,
                    color = White100,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${collection.posts.size} posts",
                    color = Gray400,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                FontAwesomeIcons.Solid.ChevronRight,
                contentDescription = "Wybierz",
                tint = Gray400,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
