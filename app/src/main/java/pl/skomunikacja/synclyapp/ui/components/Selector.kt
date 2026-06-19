package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.Hashtag
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSelector(
    availableTags: List<Tag>,
    selectedTags: List<Tag>,
    onTagsChange: (List<Tag>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = "Select tags",
                onValueChange = { },
                readOnly = true,
                label = { Text("Tags", color = Gray400) },
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
                        contentDescription = "Tags",
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
                                        contentDescription = "Selected",
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