package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ListAlt
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String = "Syncly",
    onPostCollectionsClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Teal100
            )
        },
        actions = {
            IconButton(onClick = onPostCollectionsClick) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.ListAlt,
                    contentDescription = "PostCollections",
                    tint = White100,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Black300,
            titleContentColor = White100,
            navigationIconContentColor = White100,
            actionIconContentColor = White100
        )
    )
}
