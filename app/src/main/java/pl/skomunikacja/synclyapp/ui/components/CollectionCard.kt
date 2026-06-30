package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.ui.theme.Black300
import androidx.core.graphics.toColorInt
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Star
import pl.skomunikacja.synclyapp.model.post.PostType
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100

@Composable
fun CollectionCard(
    collection: PostCollection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Black300),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(collection.color.toColorInt()))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = collection.title,
                        color = White100,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (collection.default) {
                    Icon(
                        FontAwesomeIcons.Solid.Star,
                        contentDescription = "Default collection",
                        tint = Teal100,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${collection.posts.size} posts",
                color = White100.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            if (collection.posts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(collection.posts.take(3)) { post ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Black200),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (post.postType) {
                                    PostType.TEXT -> "T"
                                    PostType.PHOTO -> "P"
                                    PostType.VIDEO -> "V"
                                    PostType.QUOTE -> "Q"
                                    PostType.LINK -> "L"
                                },
                                color = Teal100,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (collection.posts.size > 3) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Black200),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+${collection.posts.size - 3}",
                                    color = White100.copy(alpha = 0.7f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}