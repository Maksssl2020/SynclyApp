package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import pl.skomunikacja.synclyapp.helpers.PostCollectionsManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.ui.components.CollectionCard
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.ui.theme.White200
import pl.skomunikacja.synclyapp.view_model.PostCollectionsViewModel

@Composable
fun PostCollectionScreen(
    viewModel: PostCollectionsViewModel = viewModel(),
    onNavigateToPostCollectionForm: () -> Unit,
    onCollectionClick: (PostCollection) -> Unit = {},
) {
    val userPostCollections by PostCollectionsManager.userPostCollections.collectAsState()
    val loadingPostCollection by viewModel.loadingPostCollection.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
            .padding(16.dp)
    ) {
        Text(
            text = "My Post Collections",
            color = White100,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                onNavigateToPostCollectionForm()
            },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Teal100,
                contentColor = Black300,
                disabledContainerColor = Teal100.copy(alpha = 0.5f),
                disabledContentColor = Black300.copy(alpha = 0.7f)
            )
        ) {
            if (loadingPostCollection) {
                CircularProgressIndicator(
                    color = White200,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Create Collection",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

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