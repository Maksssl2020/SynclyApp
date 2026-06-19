package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.graphics.toColorInt
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.File
import compose.icons.fontawesomeicons.solid.Image
import compose.icons.fontawesomeicons.solid.Link
import compose.icons.fontawesomeicons.solid.QuoteLeft
import compose.icons.fontawesomeicons.solid.Times
import compose.icons.fontawesomeicons.solid.Video
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.model.post.PostType
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100

@Composable
fun PostTypeChip(
    postType: PostType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (icon, label) = when (postType) {
        PostType.TEXT -> FontAwesomeIcons.Solid.File to "Text"
        PostType.QUOTE -> FontAwesomeIcons.Solid.QuoteLeft to "Quote"
        PostType.PHOTO -> FontAwesomeIcons.Solid.Image to "Photo"
        PostType.VIDEO -> FontAwesomeIcons.Solid.Video to "Video"
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
                contentDescription = "Delete tag",
                tint = Gray400,
                modifier = Modifier
                    .size(12.dp)
                    .clickable { onRemove() }
            )
        }
    }
}