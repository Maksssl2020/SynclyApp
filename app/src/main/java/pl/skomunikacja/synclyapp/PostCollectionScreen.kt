package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.ui.components.CollectionCard
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.PostCollectionsViewModel

@Composable
fun PostCollectionScreen(
    onCollectionClick: (PostCollection) -> Unit = {},
    viewModel: PostCollectionsViewModel = viewModel()
) {
    val userPostCollections by ApplicationManager.userPostCollections.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
            .padding(16.dp)
    ) {
        Text(
            text = "Moje kolekcje",
            color = White100,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(userPostCollections) { collection ->
                CollectionCard(
                    collection = collection,
                    onClick = {
                        onCollectionClick(collection)
                    }
                )
            }
        }
    }
}