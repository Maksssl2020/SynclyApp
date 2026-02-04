package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.ui.components.ProfileAvatar
import pl.skomunikacja.synclyapp.ui.components.ProfileStatCard
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = ProfileViewModel()
) {
    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        authenticationData?.let { viewModel.fetchUserProfileData(it.userId) }
    }

    var isEditMode by remember { mutableStateOf(false) }
    var displayName by remember { mutableStateOf(userProfile?.displayName ?: "") }
    var username by remember { mutableStateOf(userProfile?.username ?: "") }
    var email by remember { mutableStateOf(userProfile?.email ?: "") }
    var bio by remember { mutableStateOf(userProfile?.bio ?: "") }
    var website by remember { mutableStateOf(userProfile?.website ?: "") }
    var location by remember { mutableStateOf(userProfile?.location ?: "") }
    var avatar by remember { mutableStateOf(userProfile?.avatar) }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            displayName = it.displayName ?: ""
            username = it.username ?: ""
            email = it.email ?: ""
            bio = it.bio ?: ""
            website = it.website ?: ""
            location = it.location ?: ""
            avatar = it.avatar
        }
    }

    println(userProfile)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Avatar with edit option
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Teal100),
            contentAlignment = Alignment.Center
        ) {
            ProfileAvatar(
                base64Image = avatar?.imageData,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                initials = ""
            )
            if (isEditMode) {
                IconButton(
                    onClick = { /* TODO: Implement image picker */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Black300)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Change Avatar",
                        tint = White100,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (isEditMode) {
            Spacer(modifier = Modifier.height(24.dp))

            ProfileEditField(
                label = "Display Name",
                value = displayName,
                onValueChange = { displayName = it }
            )

            ProfileEditField(
                label = "Username",
                value = username,
                onValueChange = { username = it }
            )

            ProfileEditField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )

            ProfileEditField(
                label = "Bio",
                value = bio,
                onValueChange = { bio = it },
                maxLines = 3
            )

            ProfileEditField(
                label = "Website",
                value = website,
                onValueChange = { website = it },
                keyboardType = KeyboardType.Uri
            )

            ProfileEditField(
                label = "Location",
                value = location,
                onValueChange = { location = it }
            )

        } else {
            Text(
                text = displayName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White100,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "@$username",
                fontSize = 16.sp,
                color = Gray300,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (bio.isNotEmpty()) {
                Text(
                    text = bio,
                    fontSize = 14.sp,
                    color = White100,
                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                )
            }

            if (location.isNotEmpty()) {
                Text(
                    text = "📍 $location",
                    fontSize = 14.sp,
                    color = Gray300,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (website.isNotEmpty()) {
                Text(
                    text = "🌐 $website",
                    fontSize = 14.sp,
                    color = Teal100,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Stats Row (only in display mode)
        if (!isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatCard("Posty", "42")
                ProfileStatCard("Znajomi", "128")
                ProfileStatCard("Obserwujący", "89")
            }
        }

        if (isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { isEditMode = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Gray300
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Anuluj")
                }

                Button(
                    onClick = {
                        // TODO: Save changes to backend
                        isEditMode = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal100,
                        contentColor = Black300
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Zapisz", fontWeight = FontWeight.Medium)
                }
            }
        } else {
            Button(
                onClick = { isEditMode = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal100,
                    contentColor = Black300
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edytuj profil",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White100
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Ustawienia",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Button(
                onClick = {
                    // TODO: Implement logout functionality
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = White100
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Wyloguj się",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Gray300,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray300,
                cursorColor = Teal100
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

